package com.aptner.v3.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
    ROLE_USER("입주민"),
    ROLE_ADMIN("관리자");

    private final String description;
}
