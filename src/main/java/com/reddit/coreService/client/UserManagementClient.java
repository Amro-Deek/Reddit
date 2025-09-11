package com.reddit.coreService.client;

import com.reddit.coreService.controller.dto.response.RegisterUserResponse;
import com.reddit.coreService.controller.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserManagementClient {

//    private final WebClient.Builder webClientBuilder;
//
//    @Value("${user-service.url}")
//    private String userServiceUrl; // ❌ remove final
//
//    public UserResponse getUserById(Long userId) {
//        return webClientBuilder.build()
//                .get()
//                .uri(userServiceUrl + "/internal/users/" + userId)
//                .retrieve()
//                .bodyToMono(UserResponse.class)
//                .block();
//    }

    @Autowired
    private WebClient userManagementWebClient;

//    public List<UserResponse> getUsersByIds(List<Long> userIds) {
//        return userManagementWebClient.post()
//                .uri("/internal/users/batch")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(new UserIdsRequest(userIds))
//                .retrieve()
//                .bodyToFlux(UserResponse.class)
//                .collectList()
//                .block();
//    }

    public UserResponse getUserById(Long userId) {
        try {
            return userManagementWebClient.get()
                    .uri("/internal/users/{id}", userId)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.out.println("Error calling user service: {}"+ e.getResponseBodyAsString() +e);
            throw e;
        }
//        return userManagementWebClient.get()
//                .uri("/internal/users/{userId}", userId)
//                .retrieve()
//                .bodyToMono(UserResponse.class)
//                .block();
    }
}

