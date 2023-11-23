package Selection.fret.global.security.token.service;

import Selection.fret.global.security.token.entity.RefreshToken;
import Selection.fret.global.security.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveTokenInfo(String email,String refreshToken, String accessToken){
        refreshTokenRepository.save(new RefreshToken(email,refreshToken,accessToken));
    }
    public void removeRefreshToken(String accessToken){
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
    }
}
