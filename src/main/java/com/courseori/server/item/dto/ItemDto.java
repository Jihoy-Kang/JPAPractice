package com.courseori.server.item.dto;

import com.courseori.server.location.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ItemDto {

    @Getter
    @AllArgsConstructor
    public static class Post{

//        private long memberId;

        private String title;

        private String body;

    }

    @Getter
    @AllArgsConstructor
    @Setter
    public static class Patch{
        private long itemId;

//        private long memberId;

        private String title;

        private String body;

    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        private long itemId;

//        private long memberId;

        private String title;

        private String body;


    }
}
