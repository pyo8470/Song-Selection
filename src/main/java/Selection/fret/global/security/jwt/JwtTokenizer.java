package Selection.fret.global.security.jwt;

import Selection.fret.domain.member.entity.Member;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.DateTimeException;
import java.util.*;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenizer {
    @Getter
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_16));
    }
    public  String encodeBase64SecretKey(){
        return Encoders.BASE64.encode(this.secretKey.getBytes(StandardCharsets.UTF_8));
    }
    // Access Token 생성 메서드
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String subject,
                                       Date expiration,
                                       String base64EncodedSecretKey){
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .compact();
    }

    // 검증 후 반환
    public Jws<Claims> verifySignature(String jws) {

//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(getKey())
//                    .build()
//                    .parseClaimsJws(jws);
//        } catch (ExpiredJwtException exception) {
//            throw new BusinessLogicException(ExceptionCode.JWT_TOKEN_EXPIRED);
//        }
        return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(jws);
    }
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);

        return calendar.getTime();
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getKey() {
        String base64EncodedSecretKey = encodeBase64SecretKey();

        return getKeyFromBase64EncodedKey(base64EncodedSecretKey);
    }


}
