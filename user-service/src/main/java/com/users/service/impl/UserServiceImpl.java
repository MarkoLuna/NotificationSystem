package com.users.service.impl;

import java.util.List;
import java.util.Optional;

import com.notification.api.dto.PageResponse;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import org.springframework.stereotype.Service;

import com.users.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private static final List<UserDto> USERS = List.of(
            UserDto.builder()
                    .id("1")
                    .name("john")
                    .email("john@test.com")
                    .phoneNumber("1234567890")
                    .channels(List.of(NotificationChannel.EMAIL, NotificationChannel.SMS))
                    .subscribedCategories(List.of(NotificationCategory.SPORTS, NotificationCategory.MOVIES))
                    .build(),
            UserDto.builder()
                    .id("2")
                    .name("mike")
                    .email("mike@other.com")
                    .phoneNumber("2345678901")
                    .channels(List.of(NotificationChannel.EMAIL, NotificationChannel.PUSH))
                    .subscribedCategories(List.of(NotificationCategory.MOVIES, NotificationCategory.FINANCE))
                    .build());

    @Override
    public Optional<UserDto> getUserById(String userId) {
        return USERS.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

    @Override
    public Optional<UserDto> getUserByName(String name) {
        return USERS.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return USERS.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<UserDto> getUserByPhoneNumber(String phoneNumber) {
        return USERS.stream()
                .filter(user -> user.getPhoneNumber().equals(phoneNumber))
                .findFirst();
    }

    @Override
    public PageResponse<UserDto> getAllUsers(int page, int size) {
        int totalElements = USERS.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);
        
        List<UserDto> content = fromIndex < totalElements 
                ? USERS.subList(fromIndex, toIndex) 
                : List.of();
        
        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public PageResponse<UserDto> getUsersBySubscribedCategory(NotificationCategory category, int page, int size) {
        List<UserDto> filtered = USERS.stream()
                .filter(user -> user.getSubscribedCategories().contains(category))
                .toList();
        
        int totalElements = filtered.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);
        
        List<UserDto> content = fromIndex < totalElements 
                ? filtered.subList(fromIndex, toIndex) 
                : List.of();
        
        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public PageResponse<UserDto> getUsersByChannel(NotificationChannel channel, int page, int size) {
        List<UserDto> filtered = USERS.stream()
                .filter(user -> user.getChannels().contains(channel))
                .toList();
        
        int totalElements = filtered.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);
        
        List<UserDto> content = fromIndex < totalElements 
                ? filtered.subList(fromIndex, toIndex) 
                : List.of();
        
        return PageResponse.of(content, page, size, totalElements);
    }

    @Override
    public PageResponse<UserDto> getUsersByChannelAndCategory(NotificationChannel channel, NotificationCategory category, int page, int size) {
        List<UserDto> filtered = USERS.stream()
                .filter(user -> user.getChannels().contains(channel))
                .filter(user -> user.getSubscribedCategories().contains(category))
                .toList();
        
        int totalElements = filtered.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);
        
        List<UserDto> content = fromIndex < totalElements 
                ? filtered.subList(fromIndex, toIndex) 
                : List.of();
        
        return PageResponse.of(content, page, size, totalElements);
    }

}
