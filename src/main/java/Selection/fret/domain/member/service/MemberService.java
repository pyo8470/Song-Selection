package Selection.fret.domain.member.service;

import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.member.repository.MemberRepository;
import Selection.fret.global.exception.BusinessLogicException;
import Selection.fret.global.exception.ExceptionCode;
import Selection.fret.global.security.auth.utils.CustomAuthorityUtils;
import Selection.fret.global.security.token.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final CustomAuthorityUtils authorityUtils;
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, TokenService tokenService, CustomAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authorityUtils = authorityUtils;
    }

    public Member createMember(Member member){
        // 이미 존재하는 멤버인지 이메일로 확인
        verifyExistMember(member.getEmail());
        // 통과 시 비밀번호 암호화
        String password= passwordEncoder.encode(member.getPassword());
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);
        member.setPassword(password);
        member.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);
        return memberRepository.save(member);
    }
    public void verifyExistMember(String email){
        if(memberRepository.existsByEmail(email))
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
    public boolean validatePassword(String password, String confirmPassword){
        return !password.equals(confirmPassword);
    }

    public Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

}
