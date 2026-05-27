package com.notification.notification.controller;

import com.notification.api.dto.PageResponse;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;
import com.notification.notification.dto.NotificationRequest;
import com.notification.notification.entities.Notification;
import com.notification.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Log4j2
@Validated
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Send notification")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public String sendNotification(
            @Valid @RequestBody NotificationRequest request,
            Authentication authentication) {

        log.trace("Received notification request: Channel={}, Category={}, User={}",
                request.getChannel(), request.getCategory(), authentication.getName());

        notificationService.process(request,
                ((Jwt) Objects.requireNonNull(authentication.getPrincipal())).getClaim("preferred_username"));

        log.debug("Notification request processed successfully");
        return "Notification request accepted and being processed.";
    }

    @Operation(summary = "List my notifications by the creator (user auth)")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("my")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<Notification> listMyNotifications(
            Authentication authentication,
            @Parameter(description = "Page number (0-based)", required = false)
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", required = false)
            @RequestParam(defaultValue = "10") int size) {
        return notificationService.findByCreator(
                ((Jwt) Objects.requireNonNull(authentication.getPrincipal())).getClaim("preferred_username"),
                page,
                size);
    }

    @Operation(summary = "List all notifications by the channel and creator")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<Notification> listNotifications(
            @NonNull @RequestParam NotificationChannel channel,
            @NonNull @RequestParam NotificationCategory category,
            @Parameter(description = "Page number (0-based)", required = false)
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", required = false)
            @RequestParam(defaultValue = "10") int size) {
        return notificationService.findByChannelAndCategory(channel, category, page, size);
    }
}
