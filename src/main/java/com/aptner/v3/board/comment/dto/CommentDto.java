package com.aptner.v3.board.comment.dto;

import lombok.Getter;

public class CommentDto {

    @Getter
    public static class AddRequest {
        private String content;
    }
}
