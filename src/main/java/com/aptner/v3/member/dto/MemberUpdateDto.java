package com.aptner.v3.member.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

public class MemberUpdateDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class MemberUpdateRequest {

        @Pattern(regexp = "[a-zA-Z0-9/_]{4,10}", message = "닉네임은 영어, 숫자를 포함한 4~10자리로 입력해주세요.")
        private String nickname;

        private String image;

        @Pattern(regexp = "(^\\d{3}\\d{3,4}\\d{4}$)|")
        private String phone;

    }
}
