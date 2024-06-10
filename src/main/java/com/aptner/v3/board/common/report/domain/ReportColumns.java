package com.aptner.v3.board.common.report.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportColumns {
    A("광고성 게시글인 것 같아요"),
    B("장난, 도배성 컨첸츠인 것 같아요"),
    C("음란, 욕설, 약물 등 부적절한 정보가 담겨 있어요"),
    D("사기가 의심돼요"),
    E("기타");

    private final String description;
}
