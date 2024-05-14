package com.aptner.v3.user.controller;

import com.aptner.v3.user.dto.SignUpUserDto;
import com.aptner.v3.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpUserDto.SignUpRequest request){
        log.info("signUp request: {}", request);
        userService.signUp(request);
        return new ResponseEntity<>("SIGNUP SUCCESS",HttpStatus.OK); //응답 양식 미정
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(){
        userService.logout();
        return new ResponseEntity<>("LOGOUT SUCCESS",HttpStatus.OK); //응답 양식 미정
    }
}
