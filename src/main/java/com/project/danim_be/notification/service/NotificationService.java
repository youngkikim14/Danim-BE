package com.project.danim_be.notification.service;

import com.project.danim_be.common.exception.CustomException;
import com.project.danim_be.common.exception.ErrorCode;
import com.project.danim_be.notification.repository.EmitterRepository;
import com.project.danim_be.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final static Long DEFAULT_TIMEOUT = 3600000L;
    private final static String NOTIFICATION_NAME = "notify";
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;

    public SseEmitter connectNotification(Long userId) {
        // 새로운 SseEmitter를 만든다
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        // 유저 ID로 SseEmitter를 저장한다.
        emitterRepository.save(userId, sseEmitter);

        // 세션이 종료될 경우 저장한 SseEmitter를 삭제한다.
        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        // 503 Service Unavailable 오류가 발생하지 않도록 첫 데이터를 보낸다.
        try {
            sseEmitter.send(SseEmitter.event().id("").name(NOTIFICATION_NAME).data("Connection completed"));
        } catch (IOException exception) {
            throw new CustomException(ErrorCode.FAIL_CONNECTION);
        }
        return sseEmitter;
    }

//    public void saveNotification(Long userId, Long messageId) {
//        Notification notification = notificationRepository.findByUserId(userId).orElseThrow()
//    }

    public void send(List<Long> userIdList, Long messageId) {
        // 유저 ID로 SseEmitter를 찾아 이벤트를 발생 시킨다.
        for (Long userId : userIdList) {
            emitterRepository.get(userId).ifPresentOrElse(sseEmitter -> {
                try {
                    sseEmitter.send(SseEmitter.event().id(messageId.toString()).name(NOTIFICATION_NAME).data("New notification"));
                } catch (IOException exception) {
                    // IOException이 발생하면 저장된 SseEmitter를 삭제하고 예외를 발생시킨다.
                    emitterRepository.delete(userId);
                    throw new CustomException(ErrorCode.FAIL_SEND_NOTIFICATION);
                }
            }, () -> log.info("No emitter found"));
        }
    }
}