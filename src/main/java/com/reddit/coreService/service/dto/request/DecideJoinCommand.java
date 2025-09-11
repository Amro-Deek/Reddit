package com.reddit.coreService.service.dto.request;

import com.reddit.coreService.model.RequestStatus;
import jakarta.validation.constraints.NotNull;

public record DecideJoinCommand(Long userId , RequestStatus status) {
}
