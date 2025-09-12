package com.tenco.dem_step_websocket_v01.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 채팅 메시지 데이터 전송 객체 (DTO)
 * - 클라이언트와 서버 간 구조화된 메시지 교환
 * - JSON 자동 직렬화/역직렬화 지원
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private String id;                    // String으로 변경
    private String content;
    private String sender;
    private String type;                  // String으로 변경 ("chat", "system")
    private String timestamp;             // String으로 변경

    // 내부적으로 enum으로 변환하는 메서드 추가
    public MessageType getMessageType() {
        return "system".equals(type) ? MessageType.SYSTEM : MessageType.CHAT;
    }
}
