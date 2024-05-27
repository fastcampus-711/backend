package com.aptner.v3.member.dto;


import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class MemberDto {
    private long memberId;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String image;
    private List<MemberRole> roles;

    public MemberDto(Long memberId, String username, String password, String nickname, String phone, String image, List<MemberRole> roles) {
        this.memberId = memberId;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.image = image;
        this.roles = roles;
    }

    public static MemberDto of(Long id, String username, String password, String nickname, String phone, String image, List<MemberRole> roles) {
        return new MemberDto(null, username, password, nickname, phone, image, roles);
    }

    public static MemberDto from(Member entity) {
        return new MemberDto(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getNickname(),
                entity.getPhone(),
                entity.getImage(),
                entity.getRoles()
        );
    }

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

        @Pattern(regexp = "[a-zA-Z0-9/_]{4,10}", message = "닉네임은 영어, 숫자를 포함한 4~10자리로 입력해주세요.")
        @NotNull(message = "필수 입력값입니다.")
        private String nickname;

        private String image;

        @Pattern(regexp = "(^\\d{3}\\d{3,4}\\d{4}$)|")
        private String phone;

        private List<MemberRole> roles = List.of(new MemberRole[]{MemberRole.ROLE_USER});

        @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치해야 합니다.")
        private boolean isPasswordEqual() {
            return password != null && password.equals(passwordConfirm);
        }

        public MemberDto toDto() {
            return MemberDto.of(
                    null,
                    username,
                    password,
                    nickname,
                    image,
                    phone,
                    roles
            );
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    @SuperBuilder
    public static class MemberResponse {
        private long memberId;
        private String username;
        private String nickname;
        private String phone;
        private String image;
        private List<MemberRole> roles;

        public MemberResponse(long memberId, String username, String nickname, String phone, String image, List<MemberRole> roles) {
            this.memberId = memberId;
            this.username = username;
            this.nickname = nickname;
            this.phone = phone;
            this.image = image;
            this.roles = roles;
        }

        public MemberResponse(String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt, long memberId, String username, String nickname, String phone, String image, List<MemberRole> roles) {
            this.memberId = memberId;
            this.username = username;
            this.nickname = nickname;
            this.phone = phone;
            this.image = image;
            this.roles = roles;
        }

        public static Object of(Member member) {
            return MemberResponse.builder()
                    .memberId(member.getId())
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .phone(member.getPhone())
                    .image(member.getImage())
                    .roles(member.getRoles())
                    .build();
        }
    }
}