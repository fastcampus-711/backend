package com.aptner.v3.board.menu.dto;

import lombok.Getter;
import lombok.Setter;

public class DeleteMenuDto {
    @Getter
    @Setter
    public static class Request {
        private long id;
    }
}
