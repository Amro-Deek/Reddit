package com.reddit.coreService.controller;

import com.reddit.coreService.mapper.MemberMapper;
import com.reddit.coreService.repository.MemberRepository;
import com.reddit.coreService.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;






}
