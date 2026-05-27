package com.email.notification.service.impl;

import com.email.notification.service.NotificationService;
import com.notification.api.dto.NotificationMessage;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationChannel;
import com.notification.consumer.common.service.AbstractNotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class NotificationServiceImpl extends AbstractNotificationService implements NotificationService {

    @Override
    protected NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    protected void sendNotification(UserDto user, NotificationMessage message) {
        /**
         * simulate send email message.
         */
        log.info("email sent to {} with message {}", user.getEmail(), message.getMessage());
    }
}
