package com.reddit.coreService.controller;

import com.reddit.coreService.controller.dto.request.AssignModeratorRequest;
import com.reddit.coreService.controller.dto.response.AssignModeratorResponse;
import com.reddit.coreService.mapper.ModeratorMapper;
import com.reddit.coreService.service.ModeratorService;
import com.reddit.coreService.service.dto.request.AssignModeratorCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moderator")
@RequiredArgsConstructor
public class ModeratorController {
    private final ModeratorMapper moderatorMapper ;
    private final ModeratorService moderatorService ;

    @PostMapping("/{moderatorID}/assign")
    public ResponseEntity<AssignModeratorResponse> assignModerator(@PathVariable long moderatorID
            , @Valid @RequestBody AssignModeratorRequest assignModeratorRequest){
        AssignModeratorCommand assignModeratorCommand = moderatorMapper.
                toAssignModeratorCommand(assignModeratorRequest);
        AssignModeratorResponse assignModeratorResponse = moderatorService.
                assignModerator(moderatorID,assignModeratorCommand);
        return ResponseEntity.ok(assignModeratorResponse);
    }
}
