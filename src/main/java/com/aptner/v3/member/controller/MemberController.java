package com.aptner.v3.member.controller;

import com.aptner.v3.member.dto.MemberDto;
import com.aptner.v3.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.aptner.v3.global.config.SwaggerConfig.Accesskey;

@Slf4j
@Tag(name = "회원")
@SecurityRequirement(name = Accesskey)
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<?> signUp(@Valid @RequestBody MemberDto.MemberRequest request){
        log.debug("signUp request: {}", request);
        userService.signUp(request);
        return new ResponseEntity<>("SIGNUP SUCCESS",HttpStatus.OK); //응답 양식 미정
    }

}
