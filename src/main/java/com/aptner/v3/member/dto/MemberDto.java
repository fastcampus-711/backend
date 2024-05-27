package com.aptner.v3.member.dto;


import com.aptner.v3.global.domain.BaseTimeEntity;
import com.aptner.v3.member.MemberRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

public class MemberDto extends BaseTimeEntity {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class MemberRequest {

        @Pattern(regexp = "[a-zA-Z0-9]{2,15}",
                message = "아이디는 영어, 숫자를 포함한 4~10자리로 입력해주세요.")
        @Size(min = 2, max = 15, message = "아이디를 2글자 이상 15글자 이하로 입력해주세요.")
        private String username;

        @Pattern(regexp = "[a-zA-Z0-9!@#$%^&*()]{8,15}",
                message = "비밀번호는 영어, 숫자, 특수문자(!@#$%^&*())를 포함한 8~20자리로 입력해주세요.")
        private String password;

        @NotNull(message = "필수 입력값입니다.")
        private String passwordConfirm;

        private List<MemberRole> roles;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class MemberResponse {
        private long id;
        private String username;
        private String name;
        private String phone;
        private List<MemberRole> roles;

    }
}
