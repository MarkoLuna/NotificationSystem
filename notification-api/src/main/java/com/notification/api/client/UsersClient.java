package com.notification.api.client;

import com.notification.api.dto.PageResponse;
import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.net.URIBuilder;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static java.net.http.HttpClient.newHttpClient;
import static java.time.temporal.ChronoUnit.SECONDS;

@Log4j2
@RequiredArgsConstructor
public class UsersClient {

    private final String usersServiceHost;
    private final ObjectMapper mapper;

    private final AuthorizationClient authorizationClient;
    private final String clientId;
    private final String clientSecret;

    /**
     * Get users by channel and category with pagination.
     * 
     * @param channel  notification channel
     * @param category notification category
     * @param page     page number (0-based)
     * @param size     page size
     * @return paginated response of users
     */
    public PageResponse<UserDto> getUsersByChannelAndCategory(
            @NonNull NotificationChannel channel,
            @NonNull NotificationCategory category,
            int page,
            int size) {

        var authClientCreds = generateClientCredentials();
        if (authClientCreds.isEmpty()) {
            log.error("unable to generate authentication with client credentials");
            return PageResponse.of(List.of(), page, size, 0);
        }

        HttpResponse<String> response = null;
        try (HttpClient client = newHttpClient()) {
            URI uri = new URIBuilder(usersServiceHost + "/users")
                    .addParameter("channel", channel.name())
                    .addParameter("category", category.name())
                    .addParameter("page", String.valueOf(page))
                    .addParameter("size", String.valueOf(size))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.of(10, SECONDS))
                    .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                    .headers(HttpHeaders.AUTHORIZATION, authClientCreds.get())
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            log.warn("failed to get auth credentials token", e);
            return PageResponse.of(List.of(), page, size, 0);
        } catch (InterruptedException e) {
            log.warn("failed to get auth credentials token", e);
            return PageResponse.of(List.of(), page, size, 0);
        } catch (URISyntaxException e) {
            log.warn("failed to get auth credentials token", e);
            return PageResponse.of(List.of(), page, size, 0);
        }

        if (response.statusCode() == 200
                && response.body() != null) {
            try {
                return mapper.readValue(response.body(), new TypeReference<PageResponse<UserDto>>() {
                });
            } catch (Exception e) {
                log.warn("failed to parse paginated response", e);
                return PageResponse.of(List.of(), page, size, 0);
            }
        }

        log.warn("failed to get auth credentials token response {}", response);
        return PageResponse.of(List.of(), page, size, 0);
    }

    /**
     * Generate client credentials.
     * 
     * @return optional token
     */
    private Optional<String> generateClientCredentials() {
        // TODO add this logic to an interceptor ?
        return authorizationClient.getClientCredentialsToken(clientId, clientSecret)
                .map(tokenInfo -> String.format("%s %s", tokenInfo.getTokenType(), tokenInfo.getAccessToken()));
    }
}
