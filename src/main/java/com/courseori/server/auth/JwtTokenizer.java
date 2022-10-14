package com.courseori.server.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtTokenizer {

    // encodeBase64SecretKey() 메서드는 Plain Text 형태인 Secret Key의 byte[]를 Base64 형식의 문자열로 인코딩 해줍니다.
    public String encodeBase64SecretKey(String secretKey){
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    // generateAccessToken()은 인증된 사용자에게 JWT를 최초로 발급해주기 위한 JWT 생성 메서드입니다.
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey){
        //Base64 형식 Secret Key 문자열을 이용해 Key(java.security.Key) 객체를 얻습니다.
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims) //setClaims()에는 JWT에 포함 시킬 Custom Claims를 추가합니다. Custom Claims에는 주로 인증된 사용자와 관련된 정보를 추가합니다.
                .setSubject(subject) //setSubject()에는 JWT에 대한 제목을 추가합니다.
                .setIssuedAt(Calendar.getInstance().getTime()) // setIssuedAt()에는 JWT 발행 일자를 설정하는데 파라미터 타입은 java.util.Date 타입입니다.
                .setExpiration(expiration) // setExpiration() 에는 JWT의 만료일시를 지정합니다. 파라미터 타입은 역시 java.util.Date 타입입니다.
                .signWith(key) //signWith()에 서명을 위한 Key(java.security.Key) 객체를 설정합니다.
                .compact(); // compact()를 통해 JWT를 생성하고 직렬화 합니다.
    }

    public String generateFreshToken(String subject, Date expiration, String base64EncodedSecretKey){
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    //getKeyFromBase64EncodedKey() 메서드는 JWT의 서명에 사용할 Secret Key를 생성해줍니다.
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey){
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }

    public void verifySignature(String jws, String base64EncodedSecretKey){
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key) //setSigningKey() 메서드로 서명에 사용된 Secret Key를 설정합니다.
                .build()
                .parseClaimsJws(jws); //parseClaimsJws() 메서드로 JWT를 파싱해서 Claims를 얻습니다.
    }
}
