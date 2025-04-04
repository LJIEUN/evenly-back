package com.codeisevenlycooked.evenly.global.scheduler;

import com.codeisevenlycooked.evenly.entity.Order;
import com.codeisevenlycooked.evenly.entity.OrderStatus;
import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.repository.OrderRepository;
import com.codeisevenlycooked.evenly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    //서버 시작 시 삭제 실행
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void cleanUpOnStartup() {
        log.info("서버 시작: 회원 정리 실행!");
        deleteExpiredAccounts();
        log.info("서버 시작: 미결제 주문 삭제!");
        deleteExpiredOrders();
    }

    //매일 새벽 3시 실행
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void deleteExpiredAccounts() {
        LocalDateTime now = LocalDateTime.now().minusDays(30);

        List<User> usersToDelete = userRepository.findAllByDeletedAtBefore(now);

        if (!usersToDelete.isEmpty()) {
            List<String> userIds = usersToDelete.stream()
                    .map(User::getUserId)
                    .toList();
            log.info("삭제된 회원 ID 목록: {}", userIds);

            userRepository.deleteAll(usersToDelete);
        }
    }

    @Scheduled(cron = "0 30 0 * * ?")
    @Transactional
    public void deleteExpiredOrders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusDays(1);

        List<Order> expiredOrders = orderRepository.findByStatusAndCreatedAtBefore(OrderStatus.NOT_PAID, oneDayAgo);
        orderRepository.deleteAll(expiredOrders);
    }
}
