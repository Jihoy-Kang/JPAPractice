package com.courseori.server.auth.filter;

import com.courseori.server.auth.dto.LoginDto;
import com.courseori.server.auth.jwt.JwtTokenizer;
import com.courseori.server.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//sernamePasswordAuthenticationFilter는 폼로그인 방식에서 사용하는 디폴트 Security Filter로써, 폼로그인이 아니더라도 Username/Password 기반의 인증을 처리하기 위해 UsernamePasswordAuthenticationFilter를 확장해서 구현할 수 있다.
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager; //DI 받은 AuthenticationManager는 로그인 인증 정보(Username/Password)를 전달 받아 UserDetailsService와 인터랙션 한뒤 인증 여부를 판단합니다.
        this.jwtTokenizer = jwtTokenizer; // DI 받은 JwtTokenizer는 클라이언트가 인증에 성공할 경우, JWT를 생성 및 발급하는 역할을 합니다.
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){

        ObjectMapper objectMapper = new ObjectMapper(); //클라이언트에서 전송한 Username과 Password를 DTO 클래스로 역직렬화(Deserialization)하기 위해 ObjectMapper 인스턴스를 생성
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class); //objectMapper.readValue(request.getInputStream(), LoginDto.class)를 통해 ServletInputStream 을 LoginDto 클래스의 객체로 역직렬화(Deserialization)

        //Username과 Password 정보를 포함한 UsernamePasswordAuthenticationToken을 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        return authenticationManager.authenticate(authenticationToken); //UsernamePasswordAuthenticationToken을 AuthenticationManager에게 전달하면서 인증 처리를 위임
    }

    // successfulAuthentication() 메서드는 클라이언트의 인증 정보를 이용해 인증에 성공할 경우 호출
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {
        Member member = (Member) authResult.getPrincipal();
        // authResult.getPrincipal()로 Member 엔티티 클래스의 객체를 얻습니다.
        String accessToken = delegateAccessToken(member);
        //delegateAccessToken(member) 메서드를 이용해 Access Token을 생성
        String refreshToken = delegateRefreshToken(member);
        //delegateRefreshToken(member) 메서드를 이용해 Refresh Token을 생성

        response.setHeader("Authorization", "Bearer" + accessToken);
        response.setHeader("Refresh", refreshToken);
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult); //onAuthenticationSuccess() 메서드를 호출하면 앞에서 우리가 구현한 MemberAuthenticationSuccessHandler의 onAuthenticationSuccess() 메서드가 호출
    }

    //Access Token 생성하는 구체적인 로직입니다.
    private String delegateAccessToken(Member member){
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    //Refresh Token을 생성하는 구체적인 로직입니다.
    private String delegateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }
}

