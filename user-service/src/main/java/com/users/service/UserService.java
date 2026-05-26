package com.users.service;

import com.notification.api.dto.PageResponse;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;

import java.util.Optional;

/**
 * Service used for getting user information.
 * This should be replaced by a call to User service in a real environment.
 */
public interface UserService {

    Optional<UserDto> getUserById(String userId);

    Optional<UserDto> getUserByName(String name);

    Optional<UserDto> getUserByEmail(String email);

    Optional<UserDto> getUserByPhoneNumber(String phoneNumber);

    PageResponse<UserDto> getAllUsers(int page, int size);

    PageResponse<UserDto> getUsersBySubscribedCategory(NotificationCategory category, int page, int size);

    PageResponse<UserDto> getUsersByChannel(NotificationChannel channel, int page, int size);

    PageResponse<UserDto> getUsersByChannelAndCategory(NotificationChannel channel, NotificationCategory category, int page, int size);
}
