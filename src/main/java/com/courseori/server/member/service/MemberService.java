package com.courseori.server.member.service;

import com.courseori.server.member.entity.Member;
import com.courseori.server.member.repository.MemberRepository;
import org.springframework.stereotype.Service;


@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {

        Member savedMember = memberRepository.save(member);

        // 추가된 부분

        return savedMember;
    }
}
