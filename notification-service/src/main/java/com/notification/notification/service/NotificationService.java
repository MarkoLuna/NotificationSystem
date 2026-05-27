package com.notification.notification.service;

import com.notification.api.dto.PageResponse;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.notification.notification.dto.NotificationRequest;
import com.notification.notification.entities.Notification;
import com.notification.notification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final List<NotificationStrategy> strategies;
    private final NotificationRepository notificationRepository;

    /**
     * Process notification request.
     *
     * @param request  request to process
     * @param userName user name
     */
    public void process(NotificationRequest request, String userName) {
        log.trace("Processing notification request for channel: {}", request.getChannel());

        Notification notification = Notification.builder()
                .creator(userName)
                .message(request.getMessage())
                .channel(request.getChannel())
                .category(request.getCategory())
                .build();

        notificationRepository.save(notification);

        strategies.stream()
                .filter(strategy -> strategy.supports(request.getChannel()))
                .findFirst()
                .ifPresentOrElse(
                        strategy -> strategy.send(request, userName),
                        () -> log.error("No strategy found for channel: {}", request.getChannel()));
    }

    /**
     * Find notifications by creator with pagination.
     *
     * @param creator creator to find notifications for
     * @param page    page number (0-based)
     * @param size    page size
     * @return paginated response of notifications for the creator
     */
    public PageResponse<Notification> findByCreator(String creator, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notificationPage = notificationRepository.findByCreator(creator, pageable);
        return PageResponse.of(notificationPage.getContent(), page, size, notificationPage.getTotalElements());
    }

    /**
     * Find notifications by channel and category with pagination.
     *
     * @param channel  channel to find notifications for
     * @param category category to find notifications for
     * @param page     page number (0-based)
     * @param size     page size
     * @return paginated response of notifications for the channel and category
     */
    public PageResponse<Notification> findByChannelAndCategory(NotificationChannel channel, 
        NotificationCategory category, 
        int page, 
        int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notificationPage = notificationRepository.findByChannelAndCategory(channel, category, pageable);
        return PageResponse.of(notificationPage.getContent(), page, size, notificationPage.getTotalElements());
    }
}
