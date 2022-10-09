package com.courseori.server.item.dto;

import com.courseori.server.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ItemDto {

    @Getter
    @AllArgsConstructor
    public static class Post{

        private long memberId;

        private String title;

        private String body;

        public Member getMember() {
            Member member = new Member();
            member.setMemberId(memberId);
            return member;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch{

        private long itemId;

        private String title;

        private String body;
    }

    @Getter
    @AllArgsConstructor
    public static class Response{

        private long memberId;

        private long itemId;

        private String title;

        private String body;

        public void setMember(Member member){
            this.memberId = member.getMemberId();
        }
    }

}
