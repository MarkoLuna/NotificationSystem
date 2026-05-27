package com.sms.sms.service.impl;

import com.notification.api.dto.NotificationMessage;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationChannel;
import com.notification.consumer.common.service.AbstractNotificationService;
import com.sms.sms.service.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class NotificationServiceImpl extends AbstractNotificationService implements NotificationService {

    @Override
    protected NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }

    @Override
    protected void sendNotification(UserDto user, NotificationMessage message) {
        /*
         * simulate send sms notification message.
         */
        log.info("sms notification sent to {} with message {}", user.getPhoneNumber(), message.getMessage());
    }
}
