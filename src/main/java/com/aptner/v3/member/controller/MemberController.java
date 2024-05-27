package com.aptner.v3.member.controller;

import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import com.aptner.v3.member.dto.MemberDto;
import com.aptner.v3.member.dto.MemberUpdateDto;
import com.aptner.v3.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<?> signUp(@Valid @RequestBody MemberDto.MemberRequest request) {

        return ResponseUtil.create(
                MemberDto.MemberResponse.of(userService.signUp(request))
        );
    }

    @PutMapping("/{user_id}")
    @Operation(summary = "회원 정보 수정")
    public ApiResponse<?> update(@RequestParam(value = "user_id") Long userId, @Valid @RequestBody MemberUpdateDto.MemberUpdateRequest request) {
        return ResponseUtil.update(
                MemberDto.MemberResponse.of(userService.update(userId, request))
        );
    }

}
