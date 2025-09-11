package com.reddit.userManagementService.client;
import com.reddit.userManagementService.controller.dto.response.CommunityPrivilegesResponse;
import com.reddit.userManagementService.model.CommunityPermission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CoreServiceClient {

    private final WebClient webClient;

    @Value("${core-service.url}")
    private final String coreServiceUrl;

    public CoreServiceClient(WebClient.Builder webClientBuilder,
                             @Value("${core-service.url}") String coreServiceUrl) {
        this.webClient = webClientBuilder.build();
        this.coreServiceUrl = coreServiceUrl;
    }

    /**
     * Fetches moderator privileges per community for a given user.
     * Returns empty map if user is not a moderator in any community.
     */
    public Map<Long, List<CommunityPermission>> getModeratorPrivileges(Long userId) {
        List<CommunityPrivilegesResponse> response = webClient.get()
                .uri(coreServiceUrl + "/api/community/{userId}/communities-privileges", userId)
                .retrieve()
                .bodyToFlux(CommunityPrivilegesResponse.class)
                .collectList()
                .block();

        if (response == null || response.isEmpty()) {
            return Collections.emptyMap();
        }

        return response.stream()
                .collect(Collectors.toMap(
                        CommunityPrivilegesResponse::getCommunityId,
                        r -> r.getPrivileges().stream()
                                .map(CommunityPermission::valueOf)
                                .toList()
                ));
    }
}