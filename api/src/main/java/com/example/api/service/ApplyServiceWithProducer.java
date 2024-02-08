package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyServiceWithProducer implements ApplyService{

    private final CouponCountRepository couponCountRepository;
    private final CouponRepository couponRepository;
    private final CouponCreateProducer couponCreateProducer;

    public ApplyServiceWithProducer(CouponCountRepository couponCountRepository, CouponRepository couponRepository, CouponCreateProducer couponCreateProducer) {
        this.couponCountRepository = couponCountRepository;
        this.couponRepository = couponRepository;
        this.couponCreateProducer = couponCreateProducer;
    }

    @Override
    public void apply(Long userId) {
        // 쿠폰 발급 전에 발급된 쿠폰의 개수를 증가
        Long count = couponCountRepository.increment();

        // 쿠폰 발급 가능한 개수를 초과하면 발급하지 않고 종료
        if (count > 100) {
            return;
        }

        // 직접 만들지 않고 Topic에 userId 전송
//        couponRepository.save(new Coupon(userId));
        couponCreateProducer.create(userId);
    }
}
