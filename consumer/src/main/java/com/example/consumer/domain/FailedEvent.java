package com.example.consumer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// 쿠폰 발급에 실패한 데이터를 담기 위한 클래스
@Entity
public class FailedEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    public FailedEvent() {
    }

    public FailedEvent(Long userId) {
        this.userId = userId;
    }
}
