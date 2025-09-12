package com.tenco.dem_step_websocket_v01.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * WebSocket 설정 클래스
 * - WebSocket 엔드포인트 등록
 * - CORS 설정
 * - 핸들러 매핑
 */
// TODO - 코드 수정 확인
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker // STOMP 메시지 브로커 기능 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

    /**
     * 메시지 브로커 설정
     * - 브로커는 메시지를 받아서 구독자들에게 배포하는 중간 관리자
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /**
         * enableSimpleBroker: 간단한 인메모리 브로커 활성화
         * - "/topic": 다수의 클라이언트에게 브로드캐스트하는 채널
         * - "/user": 특정 사용자에게만 개별 메시지를 보내는 채널
         *
         * 실제 운영 환경에서는 RabbitMQ, Apache ActiveMQ 등 외부 브로커 사용 가능
         */
        // /topic, /user 관례적인 접두사- 개발자가 결정 가능
        config.enableSimpleBroker("/topic", "/user");
        // /topic/chat/java  <-- 상세 설정 가능
        // /topic/chat/springboot <-- 상세 설정 가능
        // 클라이언트 측에서 보낼 때 형식
        // stompClient.send("/app/chat/java", {}, "안녕하세요. 자바 개발자 방입니다.");
        // 메시지 브로커는 @SendTo("/topic/chat/java")에 메세지 전달

        /**
         * setApplicationDestinationPrefixes: 클라이언트가 서버로 메시지를 보낼 때 사용할 prefix
         * - 클라이언트에서 "/app/chat"으로 메시지를 보내면
         * - 서버의 @MessageMapping("/chat") 메서드가 처리
         */
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * STOMP 엔드포인트 등록
     * - 클라이언트가 WebSocket 연결을 맺을 수 있는 엔드포인트 설정
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * "/ws": STOMP 연결을 위한 엔드포인트
         * - withSockJS(): SockJS fallback 옵션 활성화
         * - SockJS는 WebSocket을 지원하지 않는 브라우저에서 폴링 등으로 대체
         * - setAllowedOriginPatterns("*"): 모든 도메인에서 접속 허용 (개발용)
         */
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SockJS 지원으로 브라우저 호환성 향상
    }

}

