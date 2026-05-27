package com.push.notification.service.impl;

import com.notification.api.dto.NotificationMessage;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationChannel;
import com.notification.consumer.common.service.AbstractNotificationService;
import com.push.notification.service.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class NotificationServiceImpl extends AbstractNotificationService implements NotificationService {

    @Override
    protected NotificationChannel getChannel() {
        return NotificationChannel.PUSH;
    }

    @Override
    protected void sendNotification(UserDto user, NotificationMessage message) {
        /*
         * simulate send push notification message.
         */
        log.info("push notification sent to {} with message {}", user.getId(), message.getMessage());
    }
}
