package com.courseori.server.member.mapper;

import com.courseori.server.member.dto.MemberDto;
import com.courseori.server.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;



@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    Member memberPostToMember(MemberDto.Post requestBody);
    MemberDto.Response memberToMemberResponse(Member member);

}
