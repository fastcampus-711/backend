package com.aptner.v3.member.controller;

import com.aptner.v3.member.dto.MemberDto;
import com.aptner.v3.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody MemberDto.MemberRequest request){
        log.info("signUp request: {}", request);
        userService.signUp(request);
        return new ResponseEntity<>("SIGNUP SUCCESS",HttpStatus.OK); //응답 양식 미정
    }

}
