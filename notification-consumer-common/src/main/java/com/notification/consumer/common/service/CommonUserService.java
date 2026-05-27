package com.notification.consumer.common.service;

import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;

import java.util.List;

/**
 * Service for getting users by channel and category.
 */
public interface CommonUserService {

    /**
     * Get users by channel and category.
     * 
     * @param channel  channel to get users for
     * @param category category to get users for
     * @return list of users
     */
    List<UserDto> getUsersByChannelAndCategory(
            NotificationChannel channel,
            NotificationCategory category);
}
