package Selection.fret.global.security.auth.filter;

import Selection.fret.domain.member.dto.LoginDto;
import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.member.mapper.MemberMapper;
import Selection.fret.domain.member.service.MemberService;
import Selection.fret.global.exception.BusinessLogicException;
import Selection.fret.global.exception.ExceptionCode;
import Selection.fret.global.security.jwt.JwtTokenizer;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    private final MemberService memberService;

    private final TokenService tokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer, MemberService memberService, TokenService tokenService, MemberMapper memberMapper) {
        this.authenticationManager = authenticationManager;
        this.memberService = memberService;
        this.tokenService = tokenService;
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
        
        String accessToken = tokenService.delegateAccessToken(member);
        String refreshToken = tokenService.delegateRefreshToken(member);
        memberService.setResponseHeaders(response, accessToken, refreshToken);

        
        member = memberService.findMemberByEmail(member.getEmail());

        // 이름을 Response Body에 추가
        memberService.writeResponseBody(response, member);
        // 기존 로직은 여기서 호출
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }



}
