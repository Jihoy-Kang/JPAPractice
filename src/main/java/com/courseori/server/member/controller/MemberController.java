package com.courseori.server.member.controller;

import com.courseori.server.dto.MultiResponseDto;
import com.courseori.server.member.dto.MemberDto;
import com.courseori.server.member.mapper.MemberMapper;
import com.courseori.server.member.service.MemberService;
import com.courseori.server.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;


@RestController
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper mapper;

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

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") long memberId,
                                      @RequestBody MemberDto.Patch requestBody){
        requestBody.setMemberId(memberId);

        Member member = memberService.updateMember(mapper.memberPatchToMember(requestBody));
        MemberDto.Response response = mapper.memberToMemberResponse(member);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") long memberId){
        Member member = memberService.findMember(memberId);

        MemberDto.Response response = mapper.memberToMemberResponse(member);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(@RequestParam int page,
                                     @RequestParam int size){
        Page<Member> pageMembers = memberService.findMembers(page-1, size);
        List<Member> members = pageMembers.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(mapper.membersToMemberResponses(members), pageMembers), HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity  deleteMember(@PathVariable("member-id") long memberId){
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
