package com.reddit.userManagementService.controller.dto.request;

import java.util.List;

public record UserIdsRequest(
        List<Long> userIds) {
}
