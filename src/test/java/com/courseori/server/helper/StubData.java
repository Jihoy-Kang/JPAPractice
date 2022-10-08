package com.courseori.server.helper;

import com.courseori.server.member.dto.MemberDto;
import com.courseori.server.member.entity.Member;
import org.springframework.http.HttpMethod;

import java.util.List;

public class StubData {

    public static class MockMember{
        public static List<MemberDto.Response> getMultiResponseBody(){
            return List.of(
                    new MemberDto.Response(1L,"hgd@gmail.com", "Hong Gildong", "010-1234-1234"),
                    new MemberDto.Response(2L, "hgd2@gmail.com", "Hong Gildong2", "010-1234-1235")
                    );
        }
    }
}
