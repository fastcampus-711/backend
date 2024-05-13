package com.aptner.v3.user.controller;

import com.aptner.v3.user.dto.SignUpUserDto;
import com.aptner.v3.user.service.SignUpUserService;
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
public class SignUpUserController {

    private final SignUpUserService signUpUserService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpUserDto.SignUpRequest signUpRequest){
        log.info("signUp request: {}", signUpRequest);
        signUpUserService.signUp(signUpRequest);
        return new ResponseEntity<>("SIGNUP SUCCESS",HttpStatus.OK); //응답 양식 미정
    }
}
