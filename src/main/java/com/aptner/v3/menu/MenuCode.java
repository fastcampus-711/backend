package com.aptner.v3.menu;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 메뉴 생성을 위한 코드
 **/
@Getter
@RequiredArgsConstructor
public enum MenuCode {

    TOP_INFO("소개", "TOP_INFO", null),
    TOP_NOTICE("공지 사항", "TOP_NOTICE", null),
    TOP_MANDATORY("의무 공개", "TOP_MANDATORY", null),
    TOP_COMMUNITY("소통 공간", "TOP_COMMUNITY", null),
    TOP_COMPLAINT("민원 게시판", "TOP_COMPLAINT", null),
    TOP_FEE("관리비", "TOP_FEE", null),

    INTRO("인사말", "INTRO", null),
    APT("단지전경", "APT", null),
    CONTACT("연락처 정보", "CONTACT", null),
    COMMUNITY("커뮤니티 시설", "COMMUNITY", null),
    NOTICE("공지사항", "NOTICE", null),
    SCHEDULE("일정표", "SCHEDULE", null),
    FREE("자유게시판", "FREE", null),
    MARKET("나눔장터", "MARKET", null),
    QNA("QnA", "QNA", null),
    COMPLAINT("전체민원", "COMPLAINT", null),
    MYCOMPLAINT("나의민원", "MYCOMPLAINT", null),
    TOTALFEE("전체조회", "TOTALFEE", null),
    MYFEE("나의관리비", "MYFEE", null)
    ;

    private final String ko;
    private final String code;
    private final Class<?> clazz;

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
