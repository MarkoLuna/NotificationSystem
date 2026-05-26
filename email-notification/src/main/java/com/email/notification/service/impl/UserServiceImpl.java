package com.email.notification.service.impl;

import com.email.notification.service.UserService;
import com.notification.api.client.UsersClient;
import com.notification.api.dto.PageResponse;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersClient usersClient;

    @Override
    public List<UserDto> getUsersByChannelAndCategory(NotificationChannel channel, NotificationCategory category) {
        log.trace("Getting users by channel and category: {} {}", channel, category);

        try {
            // Use pagination with default values to fetch users
            PageResponse<UserDto> pageResponse = usersClient.getUsersByChannelAndCategory(channel, category, 0, 100);
            return pageResponse.getContent();
        } catch (Exception e) {
            log.warn("error loading users to notify", e);
            return List.of();
        }
    }
}
