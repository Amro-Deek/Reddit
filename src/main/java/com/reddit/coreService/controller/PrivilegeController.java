package com.reddit.coreService.controller;


import com.reddit.coreService.controller.dto.request.CreatePrivilegeRequest;
import com.reddit.coreService.controller.dto.response.CreatePrivilegeResponse;
import com.reddit.coreService.mapper.PrivilegeMapper;
import com.reddit.coreService.repository.PrivilegeRepository;
import com.reddit.coreService.service.PrivilegeService;
import com.reddit.coreService.service.dto.request.CreatePrivilegeCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/privilege")
@RequiredArgsConstructor
public class PrivilegeController {
    private final PrivilegeService privilegeService;
    private final PrivilegeMapper privilegeMapper;
    private final PrivilegeRepository privilegeRepository;

    @PostMapping("/create")
    public ResponseEntity<CreatePrivilegeResponse> createPrivilege(
            @Valid @RequestBody CreatePrivilegeRequest createPrivilegeRequest
    ){
        CreatePrivilegeCommand createPrivilegeCommand = privilegeMapper.fromCreatePrivilegeRequest(createPrivilegeRequest);
        CreatePrivilegeResponse createPrivilegeResponse = privilegeService.createPrivilege(createPrivilegeCommand);
        return ResponseEntity.ok(createPrivilegeResponse);
    }


}
