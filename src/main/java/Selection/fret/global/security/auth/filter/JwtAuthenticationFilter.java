package Selection.fret.global.security.auth.filter;

import Selection.fret.domain.member.dto.LoginDto;
import Selection.fret.domain.member.dto.MemberDto;
import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.member.mapper.MemberMapper;
import Selection.fret.domain.member.repository.MemberRepository;
import Selection.fret.domain.member.service.MemberService;
import Selection.fret.global.exception.BusinessLogicException;
import Selection.fret.global.exception.ExceptionCode;
import Selection.fret.global.jwt.JwtTokenizer;
import Selection.fret.global.response.SingleResponseDto;
import Selection.fret.global.security.token.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    private final MemberService memberService;

    private final TokenService tokenService;
    private final MemberMapper memberMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer, MemberService memberService, TokenService tokenService, MemberMapper memberMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        this.memberService = memberService;
        this.tokenService = tokenService;
        this.memberMapper = memberMapper;
    }



    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto;
        try {
            loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Member findMember = memberService.findMemberByEmail(loginDto.getEmail());


        if (findMember.getMemberStatus() == Member.MemberStatus.MEMBER_EXIT) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_IS_DELETED);
        }

        // (3-3)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {
        Member member = (Member) authResult.getPrincipal();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);
        setResponseHeaders(response, accessToken, refreshToken);

        
        member = memberService.findMemberByEmail(member.getEmail());

        // 이름을 Response Body에 추가
        writeResponseBody(response, member);
        // 기존 로직은 여기서 호출
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    private static void writeResponseBody(HttpServletResponse response, Member member) throws IOException {
        SingleResponseDto<String> singleResponseDto = new SingleResponseDto<>(member.getName());
        ResponseEntity<SingleResponseDto<String>> responseEntity = new ResponseEntity<>(singleResponseDto, HttpStatus.OK);

        // JSON 형태로 변환하여 Response Body에 추가
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(responseEntity.getBody());
        response.getWriter().write(responseBody);
    }

    private void setResponseHeaders(HttpServletResponse response, String accessToken, String refreshToken) {
        String headerAccessToken = "Bearer " + accessToken;

        ResponseCookie responseAccessCookie = ResponseCookie.from("access_token", accessToken)
                .sameSite("None")
                .secure(true)
                .maxAge(60 * 5) // 5분
                .path("/")
                .build();
        ResponseCookie responseRefreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .maxAge(60 * 60 * 24) // 하루
                .path("/")
                .build();
        response.setHeader("Authorization", headerAccessToken);
        response.setHeader("Refresh", refreshToken);
        response.addHeader("Set-Cookie", responseAccessCookie.toString());
        response.addHeader("Set-Cookie", responseRefreshCookie.toString());
    }

    private String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", member.getEmail());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey();

        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }

    // (6)
    private String delegateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey();
        // 이미 있는 토큰인지 판별하기 위해
        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
    }
}
