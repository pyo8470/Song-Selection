package Selection.fret.global.security.auth.handler;

import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.member.service.MemberService;
import Selection.fret.global.security.jwt.JwtTokenizer;
import Selection.fret.global.security.auth.utils.CustomAuthorityUtils;
import Selection.fret.global.security.token.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String nickName = (String) oAuth2User.getAttributes().get("name");
        String email = String.valueOf(oAuth2User.getAttributes().get("email"));


        Member member = buildOAuth2Member(nickName,email);
        if(!memberService.existsByEmail(member.getEmail())) {
            boolean isNewAccount = true;
            List<String> authorities = authorityUtils.createRoles(email);
            // db에 저장
            Member savedMember = saveMember(member);
            redirect(request, response, savedMember,isNewAccount); // 리다이렉트를 하기위한 정보들을 보내줌
        } else {
            boolean isNewAccount = false;
            Member findMember = memberService.findMemberByEmail(member.getEmail());
            redirect(request, response, findMember,isNewAccount);
        }
    }
    // TODO 수정
    private Member buildOAuth2Member(String nickName,String email) {
        Member member = new Member();
        member.setNickname(nickName);
        // 일반 유저와 구분을 위해
        member.setEmail(email);
        return member;
    }

    private Member saveMember(Member member) {
        return memberService.createMemberOAuth2(member);
    }
    private void redirect(HttpServletRequest request,
                          HttpServletResponse response,
                          Member member, boolean isNewAccount )
            throws IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        String accessToken = tokenService.delegateAccessToken(member);
        String refreshToken = tokenService.delegateRefreshToken(member);

        String uri = createURI(isNewAccount).toString();
        memberService.setResponseHeaders(response, accessToken, refreshToken);

        getRedirectStrategy().sendRedirect(request,response,uri);
    }

    private URI createURI(
                          boolean isNewAccount){
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("new", String.valueOf(isNewAccount));

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .path("/loading")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
