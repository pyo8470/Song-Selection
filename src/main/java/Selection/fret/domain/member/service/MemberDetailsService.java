package Selection.fret.domain.member.service;

import Selection.fret.domain.member.entity.Member;
import Selection.fret.global.security.auth.utils.CustomAuthorityUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

// Spring Security에서 사용자의 정보를 담는 인터페이스
// Spring Security에서 사용자의 정보를 불러오기 위해서 구현해야 하는 인터페이스
@Component
@AllArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberService memberService;
    private final CustomAuthorityUtils authorityUtils;
    @Override
    //UserDetailsService 인터페이스에는 DB에서 유저 정보를 불러오는 중요한 메소드가 존재
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member findMember = memberService.findMemberByEmail(email);
        return new MemberDetails(findMember);
    }
    private final class MemberDetails extends Member implements UserDetails{
        MemberDetails(Member member){
            setMemberId(member.getMemberId());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setBirth(member.getBirth());
            setGender(member.getGender());
            setImg(member.getImg());
            setName(member.getName());
            setPhone(member.getPhone());
            setRoles(member.getRoles());
            setMemberStatus(member.getMemberStatus());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        // 실제론 이메일을 가져온다
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
