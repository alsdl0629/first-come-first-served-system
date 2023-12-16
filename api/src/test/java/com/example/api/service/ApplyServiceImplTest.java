package com.example.api.service;

import com.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ApplyServiceImplTest {

    @Autowired
    private ApplyServiceImpl applyServiceImpl;

    @Autowired
    private ApplyServiceWithRedisIncr applyServiceWithRedisIncr;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 한번만응모() {
        applyServiceImpl.apply(1L);

        long count = couponRepository.count();

        assertThat(count).isEqualTo(1L);
    }

    // 동시에 요청을 보낼 때 100개까지 만들어지는지 확인
    @Test
    void 동시에_여러개_응모() throws InterruptedException {
        // 1000개의 요청을 동시에 보냄
        int threadCount = 1000;

        // ExecutorService는 병렬 작업을 간단하게 할 수 있게 도와주는 Java API
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        /**
         * 모든 요청이 끝날 때 까지 기다려야 하므로 CountDownLatch 사용
         * CountDownLatch는 다른 스레드에서 수행하는 작업을 기다리도록 도와주는 Java API
         */
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyServiceWithRedisIncr.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long count = couponRepository.count();

        assertThat(count).isEqualTo(100);
    }
}