package com.courseori.server.member.controller;

import com.courseori.server.helper.StubData;
import com.courseori.server.member.dto.MemberDto;
import com.courseori.server.member.entity.Member;
import com.courseori.server.member.mapper.MemberMapper;
import com.courseori.server.member.service.MemberService;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


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
    public void postMember() throws Exception {
        //given
        MemberDto.Post post = new MemberDto.Post("email@gmail.com", "1234","name","010-1234-1234");

        String content = gson.toJson(post);

        MemberDto.Response response = StubData.MockMember.getSingleResponseBody();

        given(mapper.memberPostToMember(Mockito.any(MemberDto.Post.class))).willReturn(new Member());
        given(memberService.createMember(Mockito.any(Member.class))).willReturn(new Member());
        given(mapper.memberToMemberResponse(Mockito.any(Member.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/members")
                                .with(csrf())
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
                                                        fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
                                                        fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("?????????")
                                                )
                                        ),
                                        responseFields(
                                                Arrays.asList(
                                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("????????????"),
                                                        fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
                                                        fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("?????????")
                                                )
                                        )
                                        )

                                );

    }



    @Test
    public void patchMember() throws Exception {
        long memberId = 1L;
        MemberDto.Patch patch = new MemberDto.Patch(1L,"email@gmail.com","name","010-1234-1234");

        String content = gson.toJson(patch);

        MemberDto.Response response = StubData.MockMember.getSingleResponseBody();

        given(mapper.memberPatchToMember(Mockito.any(MemberDto.Patch.class))).willReturn(new Member());
        given(memberService.updateMember(Mockito.any(Member.class))).willReturn(new Member());
        given(mapper.memberToMemberResponse(Mockito.any(Member.class))).willReturn(response);

        ResultActions actions =  mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/v1/members/{member-id}", memberId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(patch.getMemberId()))
                .andExpect(jsonPath("$.email").value(patch.getEmail()))
                .andExpect(jsonPath("$.name").value(patch.getName()))
                .andExpect(jsonPath("$.phone").value(patch.getPhone()))
                .andDo(document("patch-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                Arrays.asList(parameterWithName("member-id").description("?????? ????????? ID"))
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("?????? ????????? ID").ignored(),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("?????????").optional(),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("??????").optional(),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("?????????").optional()
                                )
                        ),
                        responseFields(
                                Arrays.asList(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("?????????")
                                )
                        )
                ));
    }

    @Test
    public void getMember() throws Exception {
        long memberId = 1L;
        MemberDto.Response response = StubData.MockMember.getSingleResponseBody();

        given(memberService.findMember(Mockito.anyLong())).willReturn(new Member());
        given(mapper.memberToMemberResponse(Mockito.any(Member.class))).willReturn(response);

        ResultActions actions =  mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/members/{member-id}", memberId)
                        .accept(MediaType.APPLICATION_JSON)
        );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.phone").value(response.getPhone()))
                .andDo(document("get-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                Arrays.asList(parameterWithName("member-id").description("?????? ????????? ID"))
                        ),
                        responseFields(
                                Arrays.asList(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("?????????")
                                )
                        )
                ));


    }

    @Test
    public void getMembers() throws Exception {
        String page = "1";
        String size = "10";

        MultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page",page);
        queryParams.add("size",size);

        Member member1 = new Member("hgd@gmail.com","Hong Gildong", "010-1234-1234", "1234");
        Member member2 = new Member("hgd2@gmail.com", "Hong Gildong2", "010-1234-1235", "1234");

        Page<Member> members = new PageImpl<>(List.of(member1,member2),
                PageRequest.of(0,10, Sort.by("member-Id").descending()), 2);

        List<MemberDto.Response> responses = StubData.MockMember.getMultiResponseBody();

        given(memberService.findMembers(Mockito.anyInt(),Mockito.anyInt())).willReturn(members);
        given(mapper.membersToMemberResponses(Mockito.anyList())).willReturn(responses);

        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/v1/members")
                        .params(queryParams)
                        .accept(MediaType.APPLICATION_JSON)
        );

        MvcResult result =
                actions
                        .andExpect(status().isOk())
                        .andDo(document("get-members",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        List.of(
                                                parameterWithName("page").description("Page ??????"),
                                                parameterWithName("size").description("Page Size")
                                        )
                                ),
                                responseFields(
                                        Arrays.asList(
                                                fieldWithPath("data[].memberId").type(JsonFieldType.NUMBER).description("????????????"),
                                                fieldWithPath("data[].email").type(JsonFieldType.STRING).description("?????????"),
                                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("??????"),
                                                fieldWithPath("data[].phone").type(JsonFieldType.STRING).description("?????????"),
                                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("????????? ??????")
                                                        .optional(),
                                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("????????? ??????")
                                                        .optional(),
                                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("????????? ?????????")
                                                        .optional(),
                                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("?????? ??? ???")
                                                        .optional(),
                                                fieldWithPath("pageInfo.totalPage").type(JsonFieldType.NUMBER).description("?????? ????????? ???")
                                                        .optional()
                                        )
                                )
                        )
                        )
                        .andReturn();
        List list = JsonPath.parse(result.getResponse().getContentAsString()).read("data");
        assertThat(list.size(), is(2));
    }

    @Test
    void deleteMember() throws Exception {

        long memberId = 1L;
        doNothing().when(memberService).deleteMember(Mockito.anyLong());

        ResultActions actions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/v1/members/{member-id}", memberId)
        );

        actions.andExpect(status().isNoContent())
                .andDo(
                        document(
                                "delete-member",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        Arrays.asList(parameterWithName("member-id").description("?????? ????????? ID"))
                                )
                        )
                );

    }
}