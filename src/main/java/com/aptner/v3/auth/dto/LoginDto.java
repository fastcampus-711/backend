package com.aptner.v3.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Builder
public record LoginDto(
        @NotNull(message = "아이디를 입력해주세요.")
        @Size(min = 2, message = "아이디는 2글자 이상 입력해주세요.")
        @Email
        String username,

        @NotNull(message = "비밀번호를 입력해 주세요.")
        @Pattern(regexp = passwordReg, message = "비밀번호는 대소문자영문+숫자(8자이상~ 15자 이하) 형식으로 입력해주세요.")
        String password
) {

    public final static String passwordReg = "^(?=.*[a-zA-Z])(?=.*\\d).{8,15}$";

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}