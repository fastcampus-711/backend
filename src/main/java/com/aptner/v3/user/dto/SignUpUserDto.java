package com.aptner.v3.user.dto;


import lombok.*;

public class SignUpUserDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Request{
        private String username;
        private String password;
        private String role;

    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Response{
        private long id;
        private String username;
        private String role;

    }
}
