package Selection.fret.global.security.token.service;

import Selection.fret.domain.member.entity.Member;
import Selection.fret.global.security.jwt.JwtTokenizer;
import Selection.fret.global.security.token.entity.RefreshToken;
import Selection.fret.global.security.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenizer jwtTokenizer;

//    @Transactional
//    public void saveTokenInfo(String email,String refreshToken, String accessToken){
//        refreshTokenRepository.save(new RefreshToken(email,refreshToken,accessToken));
//    }
//    public void removeRefreshToken(String accessToken){
//        refreshTokenRepository.findByAccessToken(accessToken)
//                .ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
//    }

    public String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", member.getMemberId());
        claims.put("roles", member.getRoles());
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey();

        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }
    public String delegateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey();

        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
    }

}
