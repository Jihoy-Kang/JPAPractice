package com.courseori.server.helper;

import com.courseori.server.item.entity.Item;
import com.courseori.server.member.dto.MemberDto;
import com.courseori.server.member.entity.Member;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class StubData {
    static List<Item> itemList = new ArrayList<>();
    public static class MockMember{
        public static List<MemberDto.Response> getMultiResponseBody(){
            return List.of(
                    new MemberDto.Response(1L,"hgd@gmail.com", "Hong Gildong", "010-1234-1234", itemList),
                    new MemberDto.Response(2L, "hgd2@gmail.com", "Hong Gildong2", "010-1234-1235", itemList)
                    );
        }

        public static MemberDto.Response getSingleResponseBody(){
            return new MemberDto.Response(1L,"hgd@gmail.com", "Hong Gildong", "010-1234-1234", itemList);
        }
    }
}
