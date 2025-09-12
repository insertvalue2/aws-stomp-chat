package com.tenco.dem_step_websocket_v01.chat;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA를 활용한 데이터 접근 인터페이스
 * - JpaRepository<Entity, PK타입>을 상속하면 기본 CRUD 자동 제공
 * - findAll(), save(), findById() 등 메서드 사용 가능
 */
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    // 기본 메서드만으로도 충분하므로 추가 메서드 정의 불필요
}