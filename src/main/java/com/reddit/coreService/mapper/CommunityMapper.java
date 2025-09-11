package com.reddit.coreService.mapper;

import com.reddit.coreService.controller.dto.request.CreateCommunityRequest;
import com.reddit.coreService.controller.dto.response.CommunityResponse;
import com.reddit.coreService.model.Community;
import com.reddit.coreService.service.dto.request.CreateCommunityCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    CreateCommunityCommand fromCreateCommunityRequest(CreateCommunityRequest createCommunityRequest);
    CommunityResponse fromCommunity(Community community);
}
