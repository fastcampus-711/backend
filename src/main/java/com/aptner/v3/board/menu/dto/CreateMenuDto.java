package com.aptner.v3.board.menu.dto;

import com.aptner.v3.board.menu.domain.Menu;
import lombok.Getter;
import lombok.Setter;

public class CreateMenuDto {
    @Getter
    @Setter
    public static class Request {
        private String name;

        public Menu toEntity() {
            return new Menu(name);
        }
    }
}
