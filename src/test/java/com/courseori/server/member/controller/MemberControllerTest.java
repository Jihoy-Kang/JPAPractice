package com.courseori.server.member.controller;

import com.courseori.server.member.dto.MemberDto;
import com.courseori.server.member.entity.Member;
import com.courseori.server.member.mapper.MemberMapper;
import com.courseori.server.member.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberMapper mapper;

    @Test
    void postMember() throws Exception {
        //given
        MemberDto.Post post = new MemberDto.Post("email@gmail.com","name","010-1234-1234");

        String content = gson.toJson(post);

        MemberDto.Response response = new MemberDto.Response(1l,"email@gmail.com","name","010-1234-1234");

        given(mapper.memberPostToMember(Mockito.any(MemberDto.Post.class))).willReturn(new Member());
        given(memberService.createMember(Mockito.any(Member.class))).willReturn(new Member());
        given(mapper.memberToMemberResponse(Mockito.any(Member.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/members")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content));

                        actions
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.email").value(post.getEmail()))
                                .andExpect(jsonPath("$.name").value(post.getName()))
                                .andExpect(jsonPath("$.phone").value(post.getPhone()))
                                .andDo(document("post-member",
                                        preprocessRequest(prettyPrint()),
                                        preprocessResponse(prettyPrint()),
                                        requestFields(
                                                List.of(
                                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("연락처")
                                                )
                                        ),
                                        responseFields(
                                                Arrays.asList(
                                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원번호"),
                                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("연락처")
                                                )
                                        )
                                        )

                                );

    }



//    @Test
//    void patchMember() {
//    }
//
//    @Test
//    void getMember() {
//    }
//
//    @Test
//    void getMembers() {
//    }
//
//    @Test
//    void deleteMember() {
//    }
}