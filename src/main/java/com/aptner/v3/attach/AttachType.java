package com.aptner.v3.attach;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum AttachType {

    PROFILE("profile", 10_000_000L, List.of("jpg", "jpeg", "png")), // 10MB
    FILE("file", 10_000_000L, List.of("jpg", "jpeg", "png", "pdf")),
    CSV("csv", 10_000_000L, List.of("csv"));
    private final String location;
    private final Long maxSize;
    private final List<String> allowedExtension;
}
