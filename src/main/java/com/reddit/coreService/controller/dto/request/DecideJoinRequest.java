package com.reddit.coreService.controller.dto.request;

import com.reddit.coreService.model.RequestStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DecideJoinRequest(@NotNull
                                Long userId ,@NotNull @Valid RequestStatus status) {
}
