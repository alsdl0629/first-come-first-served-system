package com.example.api.service;

import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class OnlyOneUserApplyService implements ApplyService {

    private final CouponCountRepository couponCountRepository;
    private final CouponRepository couponRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    public OnlyOneUserApplyService(CouponCountRepository couponCountRepository, CouponRepository couponRepository, CouponCreateProducer couponCreateProducer, AppliedUserRepository appliedUserRepository) {
        this.couponCountRepository = couponCountRepository;
        this.couponRepository = couponRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    @Override
    public void apply(Long userId) {
        Long isApplied = appliedUserRepository.add(userId);

        // 1이 아니면 쿠폰을 발급했던 유저
        if (isApplied != 1) {
            return;
        }

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
