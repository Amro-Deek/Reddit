package com.reddit.coreService.service;

import com.reddit.coreService.controller.dto.response.CreatePrivilegeResponse;
import com.reddit.coreService.exceptions.ConflictException;
import com.reddit.coreService.exceptions.InvalidInputException;
import com.reddit.coreService.mapper.PrivilegeMapper;
import com.reddit.coreService.model.Moderator;
import com.reddit.coreService.model.ModeratorPrivilege;
import com.reddit.coreService.model.Privilege;
import com.reddit.coreService.model.PrivilegeName;
import com.reddit.coreService.repository.ModeratorPrivilegeRepository;
import com.reddit.coreService.repository.ModeratorRepository;
import com.reddit.coreService.repository.PrivilegeRepository;
import com.reddit.coreService.service.dto.request.CreatePrivilegeCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper;
    private final ModeratorRepository moderatorRepository;
    private final ModeratorPrivilegeRepository moderatorPrivilegeRepository;


    public CreatePrivilegeResponse createPrivilege(CreatePrivilegeCommand createPrivilegeCommand) {
        // 1. Parse & validate privilege name
        PrivilegeName privilegeName;
        try {
            privilegeName = PrivilegeName.valueOf(
                    createPrivilegeCommand.name().toString().toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException(
                    "Invalid privilege name: " + createPrivilegeCommand.name()
            );
        }

        // 2. Check for duplicate privilege
        Privilege existingPrivilege = privilegeRepository.findByName(privilegeName);
        if (existingPrivilege != null) {
            throw new ConflictException(
                    "Privilege already exists with name: " + privilegeName
            );
        }

        // 3. Create and persist new privilege
        Privilege privilege = new Privilege();
        privilege.setName(privilegeName);
        privilege.setDescription(createPrivilegeCommand.description());
        privilege = privilegeRepository.save(privilege);

        // 4. Auto-assign this privilege to all "community creators" (moderators without an assignedBy)
        List<Moderator> communityCreators = moderatorRepository.findByAssignedByIsNull();
        for (Moderator creator : communityCreators) {
            ModeratorPrivilege mp = new ModeratorPrivilege();
            mp.setModerator(creator);
            mp.setPrivilege(privilege);
            moderatorPrivilegeRepository.save(mp);
        }

        // 5. Map and return response DTO
        return privilegeMapper.fromPrivilege(privilege);
    }

}
