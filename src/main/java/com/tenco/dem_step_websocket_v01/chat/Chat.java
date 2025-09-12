package com.tenco.dem_step_websocket_v01.chat;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 채팅 메시지를 저장하는 엔티티
 * - 폴링 방식에서는 모든 메시지가 DB에 저장되어야 함
 * - 클라이언트가 요청할 때마다 최신 데이터를 조회하기 위함
 */
@NoArgsConstructor // JPA용 기본 생성자
@Getter // Lombok: getter 메서드 자동 생성
@Table(name = "chat_tb") // 테이블명 명시적 지정
@Entity // JPA 엔티티 선언
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Integer id;

    private String msg; // 채팅 메시지 내용

    @Builder
    public Chat(Integer id, String msg) {
        this.id = id;
        this.msg = msg;
    }
}