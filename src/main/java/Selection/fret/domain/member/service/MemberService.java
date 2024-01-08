package Selection.fret.domain.member.service;

import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.member.repository.MemberRepository;
import Selection.fret.global.exception.BusinessLogicException;
import Selection.fret.global.exception.ExceptionCode;
import Selection.fret.global.response.SingleResponseDto;
import Selection.fret.global.security.auth.utils.CustomAuthorityUtils;
import Selection.fret.global.security.token.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public Member createMember(Member member){
        // 이미 존재하는 멤버인지 이메일로 확인
        verifyExistMember(member.getEmail());
        // 통과 시 비밀번호 암호화
        String password= passwordEncoder.encode(member.getPassword());
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);
        member.setPassword(password);
        member.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);
        member.setProvider(Member.Provider.LOCAL);
        return memberRepository.save(member);
    }
    //Todo : 추가 입력사항 수정 필요
    public Member createMemberOAuth2(Member member){
        log.info(member.getEmail());
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);
        member.setProvider(Member.Provider.GOOGLE);
        return memberRepository.save(member);
    }
    public void verifyExistMember(String email){
        if(memberRepository.existsByEmail(email))
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
    public boolean existsByEmail(String email){
        // 이메일이 존재
        return memberRepository.existsByEmail(email);
    }
    public boolean validatePassword(String password, String confirmPassword){
        return !password.equals(confirmPassword);
    }

    public Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public void writeResponseBody(HttpServletResponse response, Member member) throws IOException {
        SingleResponseDto<String> singleResponseDto = new SingleResponseDto<>(member.getName());
        ResponseEntity<SingleResponseDto<String>> responseEntity = new ResponseEntity<>(singleResponseDto, HttpStatus.OK);

        // JSON 형태로 변환하여 Response Body에 추가
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(responseEntity.getBody());
        response.getWriter().write(responseBody);
    }

    public void setResponseHeaders(HttpServletResponse response, String accessToken, String refreshToken) {
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

        response.setHeader("Refresh", refreshToken);
        response.addHeader("Set-Cookie", responseAccessCookie.toString());
        response.addHeader("Set-Cookie", responseRefreshCookie.toString());
    }

}
