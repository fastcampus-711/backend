package com.aptner.v3.menu;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuCode {

    INFO("소개", "INFO"),
    NOTICE("공지 사항", "NOTICE"),
    MANDATORY("의무 공개", "MANDATORY"),
    COMMUNITY("소통 공간", "COMMUNITY"),
    COMPLAINT("민원 게시판", "COMPLAINT"),
    FEE("관리비", "FEE"),

    SUB_INTRO("인사말", "SUB_INTRO"),
    SUB_APT("단지전경", "SUB_APT"),
    SUB_CONTACT("연락처 정보", "SUB_CONTACT"),
    SUB_COMMUNITY("커뮤니티 시설", "SUB_COMMUNITY"),
    SUB_NOTICE("공지사항", "SUB_NOTICE"),
    SUB_SCHEDULE("일정표", "SUB_SCHEDULE"),
    SUB_FREE("자유게시판", "SUB_FREE"),
    SUB_MARKET("나눔장터", "SUB_MARKET"),
    SUB_QNA("QnA", "SUB_QNA"),
    SUB_COMPLAINT("전체민원", "SUB_COMPLAINT"),
    SUB_MYCOMPLAINT("나의민원", "SUB_MYCOMPLAINT"),
    SUB_TOTALFEE("전체조회", "SUB_TOTALFEE"),
    SUB_MYFEE("나의관리비", "SUB_MYFEE")
    ;

    private final String ko;
    private final String code;

    @JsonCreator
    public static MenuCode from(String val) {
        for (MenuCode m : MenuCode.values()) {
            if (m.name().equals(val)) {
                return m;
            }
        }
        return null;
    }
}
