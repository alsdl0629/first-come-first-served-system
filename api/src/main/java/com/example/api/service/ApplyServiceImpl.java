package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyServiceImpl implements ApplyService {

    private final CouponRepository couponRepository;

    public ApplyServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    // 쿠폰 발급 함수
    @Override
    public void apply(Long userId) {
        // 쿠폰의 개수를 가져옴
        long count = couponRepository.count();

        // 쿠폰 발급 가능한 개수를 초과하면 발급하지 않고 종료
        if (count > 100) {
            return;
        }

        couponRepository.save(new Coupon(userId));
    }
}
