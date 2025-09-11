package com.reddit.coreService.controller.dto.response;

import com.reddit.coreService.model.Moderator;
import com.reddit.coreService.model.RequestStatus;
import jakarta.validation.constraints.NotNull;

public record DecideJoinResponse(
        Moderator assignedBy , RequestStatus status ) {
}
