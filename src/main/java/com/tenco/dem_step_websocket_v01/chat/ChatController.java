package com.tenco.dem_step_websocket_v01.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

/**
 * STOMP 기반 채팅 컨트롤러
 * - 기존의 WebSocketHandler의 복잡한 로직이 간단한 어노테이션으로 대체
 * - 메시지 라우팅, 브로드캐스트, 세션 관리가 모두 자동화
 */
@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatService chatService;

    /**
     * 채팅 목록 페이지 (기존과 동일)
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("models", chatService.findAll());
        return "index";
    }

    /**
     * 폼에서 메시지 저장 처리 (기존 방식과의 호환성 유지)
     */
    @PostMapping("/")
    public String save(String msg) {
        chatService.save(msg);
        return "redirect:/";
    }

    /**
     * 메시지 작성 폼 페이지 (기존과 동일)
     */
    @GetMapping("/save-form")
    public String saveForm(){
        return "save-form";
    }

    /**
     * STOMP DTO 메시지 처리
     * 매개변수: JSON → ChatMessageDto 자동 변환
     * 반환값: ChatMessageDto → JSON 자동 직렬화
     */
    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public ChatMessageDto sendMessage(ChatMessageDto messageDto) {
        log.info("STOMP DTO 메시지 수신: {}", messageDto);

        try {
            // 1. 메시지 내용 검증
            if (messageDto == null ||
                    messageDto.getContent() == null ||
                    messageDto.getContent().trim().isEmpty()) {
                return createErrorMessage("빈 메시지는 전송할 수 없습니다.");
            }

            // 2. 발신자 검증 및 기본값 설정
            String sender = messageDto.getSender();
            if (sender == null || sender.trim().isEmpty()) {
                sender = "익명";
            }

            // 3. 데이터베이스에 메시지 저장
            Chat savedChat = chatService.save(messageDto.getContent().trim());

            // 4. 응답 DTO 생성 - 간단하게 toString() 사용
            ChatMessageDto responseDto = ChatMessageDto.builder()
                    .id(savedChat.getId().toString())          // Integer → String 변환
                    .content(savedChat.getMsg())
                    .sender(sender)
                    .type("CHAT")                              // String 타입으로 설정
                    .timestamp(LocalDateTime.now().toString()) // 간단하게 toString() 사용
                    .build();

            log.info("브로드캐스트할 DTO: {}", responseDto);
            return responseDto;

        } catch (Exception e) {
            log.error("메시지 처리 실패", e);
            return createErrorMessage("메시지 저장에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 에러 메시지 생성 헬퍼 메서드
     */
    private ChatMessageDto createErrorMessage(String errorContent) {
        return ChatMessageDto.builder()
                .id(System.currentTimeMillis() + "")        // 임시 ID (밀리초)
                .content(errorContent)
                .sender("SYSTEM")
                .type("SYSTEM")                             // String 타입
                .timestamp(LocalDateTime.now().toString())  // 간단하게 toString()
                .build();
    }
}