package com.reddit.userManagementService.mapper;

import com.reddit.userManagementService.controller.dto.response.FollowResponse;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.service.dto.response.FollowDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-07T11:34:31+1400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class FollowMapperImpl implements FollowMapper {

    @Override
    public FollowDTO fromUser(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;

        id = user.getId();
        username = user.getUsername();

        FollowDTO followDTO = new FollowDTO( id, username );

        return followDTO;
    }

    @Override
    public FollowResponse fromFollowDTO(FollowDTO followDTO) {
        if ( followDTO == null ) {
            return null;
        }

        Long id = null;
        String username = null;

        id = followDTO.id();
        username = followDTO.username();

        FollowResponse followResponse = new FollowResponse( id, username );

        return followResponse;
    }
}
