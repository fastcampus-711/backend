package com.aptner.v3.menu.menu;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuName {

    INFO("소개", "INFO"),
    NOTICE("공지 사항", "NOTICE"),
    MANDATORY("의무 공개", "MANDATORY"),
    COMMUNITY("소통 공간", "COMMUNITY"),
    COMPLAINT("민원 게시판", "COMPLAINT"),
    FEE("관리비", "FEE");

    private final String ko;
    private final String code;

    @JsonCreator
    public static MenuName from(String val) {
        for (MenuName m : MenuName.values()) {
            if (m.name().equals(val)) {
                return m;
            }
        }
        return null;
    }
}
