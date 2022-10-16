package com.courseori.server.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomAuthorityUtils {

    @Value("${mail.address.admin}") //application.yml에 추가한 프로퍼티를 가져오는 표현식
    private String adminMailAddress; //@Value("${프로퍼티 경로}")의 표현식 형태로 작성하면 application.yml에 정의되어 있는 프로퍼티의 값을 클래스 내에서 사용할 수 있다.

    //AuthorityUtils 클래스를 이용해서 관리자용 권한 목록을 List<GrantedAuthority> 객체로 미리 생성
    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN","ROLE_USER");

    private final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("USER_ROLES");

    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN","USER");

    private final List<String> USER_ROLES_STRING = List.of("USER");


    public List<GrantedAuthority> createAuthorities(String email){
        if(email.equals(adminMailAddress)){
            return ADMIN_ROLES;
        }
        return USER_ROLES;
    }

    public List<String> createRoles(String email){
        if(email.equals(adminMailAddress)){
            return ADMIN_ROLES_STRING;
        }
        return USER_ROLES_STRING;
    }
}
