package com.courseori.server.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class MemberDto {
    @Getter
    @AllArgsConstructor // TODO 테스트를 위해 추가됨
    public static class Post {

        private String email;
        private String name;
        private String phone;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch{
        private long memberId;
        private String email;
        private String name;
        private String phone;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        private long memberId;
        private String email;
        private String name;
        private String phone;

    }
}
