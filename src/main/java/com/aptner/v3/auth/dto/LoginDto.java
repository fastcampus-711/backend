package com.aptner.v3.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class LoginDto {

    public final static String passwordReg = "^(?=.*[a-zA-Z])(?=.*\\d).{8,15}$";

    @NotNull(message = "아이디를 입력해주세요.")
    @Size(min = 2, message = "아이디는 2글자 이상 입력해주세요.")
    @Email
    private String username;

    @NotNull(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = passwordReg, message = "비밀번호는 대소문자영문+숫자(8자이상~ 15자 이하) 형식으로 입력해주세요.")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}