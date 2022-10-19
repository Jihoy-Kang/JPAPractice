package com.courseori.server.auth.filter;

import com.courseori.server.auth.jwt.JwtTokenizer;
import com.courseori.server.auth.utils.CustomAuthorityUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
//Spring Security에서는 (1)과 같이 OncePerRequestFilter를 확장해서 request 당 한번만 실행되는 Security Filter를 구현할 수 있습니다.
//JWT의 검증은 request 당 단 한번만 수행하면 되기 때문에 JWT 전용 Filter로 만들기에는 OncePerRequestFilter 를 이용하는 것이 적절하다고 볼 수 있습니다.
//인증과 관련된 Filter는 성공이냐 실패냐를 단 한번만 판단하면 됩니다. 성공도 아니고 실패도 아닌 어중간한 결과는 존재하지 않으며 여러번 판단할 필요도 없는 것입니다.
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, Object> claims = verifyJws(request); // (3)
        setAuthenticationToContext(claims);      // (4)

        filterChain.doFilter(request, response); // (5)
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // OncePerRequestFilter의 shouldNotFilter()를 오버라이드 한 것으로, 특정 조건에 부합하면(true이면) 해당 Filter의 동작을 수행하지 않고 다음 Filter로 건너뛰도록 해줍니다.
        String authorization = request.getHeader("Authorization");  // (6-1) Authorization header의 값을 얻은 후

        return authorization == null || !authorization.startsWith("Bearer");  // (6-2)Authorization header의 값이 null이거나 Authorization header의 값이 “Bearer”로 시작하지 않는다면 해당 Filter의 동작을 수행하지 않도록 정의
    } //이 말의 의미는 JWT가 Authorization header에 포함되지 않았다면 JWT 자격증명이 필요하지 않은 리소스에 대한 요청이라고 판단하고 다음(Next) Filter로 처리를 넘기는 것입니다.
    //만일 JWT 자격 증명이 필요한 리소스 요청인데 실수로 JWT를 포함하지 않았다 하더라도 이 경우에는 Authentication이 정상적으로 SecurityContext에 저장되지 않은 상태이기 때문에 다른 Security Filter를 거쳐 결국 Exception을 던지게 될 것입니다.

    private Map<String, Object> verifyJws(HttpServletRequest request){ //JWT를 검증하는데 사용되는 private 메서드
        String jws = request.getHeader("Authorization").replace("Bearer", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()); // JWT 서명(Signature)을 검증하기 위한 Secret Key를 얻습니다.
        Map<String,Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody(); // JWT에서 Claims를 파싱합니다.
        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims){
        String username = (String) claims.get("username"); //JWT에서 파싱한 Claims에서 username을 얻습니다.
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List)claims.get("roles")); //JWT의 Claims에서 얻은 권한 정보를 기반으로 List<GrantedAuthority 를 생성합니다.
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);  // (4-3)username과 List<GrantedAuthority 를 포함한 Authentication 객체를 생성합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication); // (4-4)SecurityContext에 Authentication 객체를 저장합니다.
    }
}
