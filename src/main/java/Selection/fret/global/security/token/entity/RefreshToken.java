package Selection.fret.global.security.token.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
// 중요한 데이터 x, 주기적으로 데이터 무효 -> Redis 사용
// key-value 쌍으로 데이터 관리 가능, In-Memory DB

// Redis에 저장할 객체
@AllArgsConstructor
@Getter
@RedisHash(value = "jwtToken",timeToLive = 60*60*24*3)
public class RefreshToken {
    @Id
    private String email;
    private String refreshToken;

    // 해당 필드 값으로 데이터를 찾기 위함
    @Indexed
    private String accessToken;
}
