package com.reddit.coreService.mapper;

import com.reddit.coreService.controller.dto.request.DecideJoinRequest;
import com.reddit.coreService.controller.dto.response.DecideJoinResponse;
import com.reddit.coreService.controller.dto.response.MemberPendingRequestResponse;
import com.reddit.coreService.model.MemberRequest;
import com.reddit.coreService.service.dto.request.DecideJoinCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberRequestMapper {

//    @Mapping(source = "id", target = "communityId")
//    @Mapping(source = "id", target = "moderatorId")
    MemberPendingRequestResponse toMemberPendingRequestResponse(MemberRequest memberRequest);


    DecideJoinCommand toDecideJoinCommand(DecideJoinRequest decideJoinRequest);

    DecideJoinResponse toDecideJoinResponse(MemberRequest memberRequest);

}
