package com.reddit.coreService.controller.dto.response;

import com.reddit.coreService.model.RequestStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MemberPendingRequestResponse(@NotNull(message = "User must not be null")
                                           UserResponse userResponse, long communityId , RequestStatus status,
                                           LocalDateTime createdAt, Long moderatorId) {
}
