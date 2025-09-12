package com.tenco.dem_step_websocket_v01.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 채팅 관련 비즈니스 로직을 처리하는 서비스
 * - 폴링에서는 매번 전체 메시지를 조회해야 함
 * - 최신 메시지가 위에 오도록 내림차순 정렬
 */
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성
@Service
public class ChatService {
    private final ChatRepository chatRepository;

    /**
     * 새로운 채팅 메시지 저장
     * @param msg 사용자가 입력한 메시지
     * @return 저장된 Chat 엔티티
     */
    @Transactional // 쓰기 작업이므로 읽기전용 해제
    public Chat save(String msg) {
        Chat chat = Chat.builder().msg(msg).build();
        return chatRepository.save(chat);
    }

    /**
     * 모든 채팅 메시지를 최신순으로 조회
     * @return 채팅 메시지 리스트
     */
    public List<Chat> findAll(){
        // ID 기준 내림차순 정렬 (최신 메시지가 하단으로 처리)
        Sort desc = Sort.by(Sort.Direction.ASC, "id");
        return chatRepository.findAll(desc);
    }
}