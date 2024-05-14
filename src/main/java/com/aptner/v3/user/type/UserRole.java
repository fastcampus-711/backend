package com.aptner.v3.user.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("입주민"),
    ADMIN("관리자"),
    RELATED_COMPANY("관계사"),
    OWNER("소유주");

    private final String description;
}
