package com.aptner.v3.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.validation.annotation.Validated;

public class SignUpUserDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Request{

        @Pattern(regexp = "[a-zA-Z0-9]{4,10}",
                message = "아이디는 영어, 숫자를 포함한 4~10자리로 입력해주세요.")
        private String username;

        @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()]{8,20}",
                message = "비밀번호는 영어, 숫자, 특수문자(!@#$%^&*())를 포함한 8~20자리로 입력해주세요.")
        private String password;

        @NotNull(message = "필수 입력값입니다.")
        private String passwordConfirm;

        //@NotNull(message = "필수 입력값입니다.")
        private String role;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Response{
        private long id;
        private String username;
        private String role;

    }
}
