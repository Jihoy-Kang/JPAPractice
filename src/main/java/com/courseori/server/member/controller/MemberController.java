package com.courseori.server.member.controller;

import com.courseori.server.member.dto.MemberDto;
import com.courseori.server.member.mapper.MemberMapper;
import com.courseori.server.member.service.MemberService;
import com.courseori.server.member.entity.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/members")
public class MemberController {

    MemberService memberService;
    MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postMember(@RequestBody MemberDto.Post requestBody) {
        Member member = mapper.memberPostToMember(requestBody);


        Member createdMember = memberService.createMember(member);
        MemberDto.Response response = mapper.memberToMemberResponse(createdMember);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
