package com.aptner.v3.board.menu.dto;

import lombok.Getter;
import lombok.Setter;

public class UpdateMenuDto {
    @Getter
    @Setter
    public static class Request {
        private long targetId;
        private String to;
    }
}
