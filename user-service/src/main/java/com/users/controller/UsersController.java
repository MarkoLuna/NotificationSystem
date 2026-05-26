package com.users.controller;

import com.notification.api.dto.PageResponse;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for user lookup and management")
public class UsersController {

    private final UserService userService;

    @Operation(
            summary = "Get users by channel and category",
            description = "Retrieves a paginated list of users subscribed to a specific notification channel and category.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved subscribers",
                            content = @Content(schema = @Schema(implementation = PageResponse.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<UserDto> getUsersByChannelAndCategory(
            @Parameter(description = "Notification channel (e.g., EMAIL, SMS)", required = true)
            @RequestParam NotificationChannel channel,
            @Parameter(description = "Notification category (e.g., SPORTS, FINANCE)", required = true)
            @RequestParam NotificationCategory category,
            @Parameter(description = "Page number (0-based)", required = false)
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", required = false)
            @RequestParam(defaultValue = "10") int size) {
        log.debug("Requesting users for channel: {} and category: {}, page: {}, size: {}", channel, category, page, size);
        return userService.getUsersByChannelAndCategory(channel, category, page, size);
    }
}
