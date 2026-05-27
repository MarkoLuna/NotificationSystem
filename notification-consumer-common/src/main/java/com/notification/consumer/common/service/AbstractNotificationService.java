package com.notification.consumer.common.service;

import com.notification.api.dto.NotificationMessage;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationChannel;
import com.notification.consumer.common.service.CommonUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * Abstract base class for notification services using the Template Method pattern.
 * Handles common logic for processing notification messages and delegates channel-specific
 * sending to concrete implementations.
 */
@Log4j2
@RequiredArgsConstructor
public abstract class AbstractNotificationService {

    private final CommonUserService userService;

    /**
     * Process notification message by fetching subscribers and sending notifications.
     * 
     * @param message the notification message to process
     */
    public void processNotificationMessage(NotificationMessage message) {
        log.info("Sending {} notification from user '{}': [{}] {}",
                getChannel(), message.getUserName(), message.getCategory(), message.getMessage());

        userService.getUsersByChannelAndCategory(getChannel(), message.getCategory())
                .stream()
                // filter out user who created the notification
                .filter(user -> !message.getUserName().equals(user.getName()))
                .forEach(user -> sendNotification(user, message));
    }

    /**
     * Get the notification channel for this service.
     * 
     * @return the notification channel
     */
    protected abstract NotificationChannel getChannel();

    /**
     * Send notification to a specific user.
     * Concrete implementations must provide channel-specific sending logic.
     * 
     * @param user    the user to send notification to
     * @param message the notification message
     */
    protected abstract void sendNotification(UserDto user, NotificationMessage message);
}
