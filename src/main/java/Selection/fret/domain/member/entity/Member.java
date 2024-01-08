package Selection.fret.domain.member.entity;

import Selection.fret.domain.audit.Auditable;
import Selection.fret.global.regexp.RegexPattern;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Member extends Auditable {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Email
    @Column(unique = true)
    private String email;

    // 비밀번호 제약조건 : 특수문자 영어 숫자
    @Pattern(regexp = RegexPattern.REGEXP_USER_PW_TYPE, message = "비밀번호는 영어,숫자,특수문자 조합입니다.")
    private String password;

    @Column String nickname;

    @Column(length = 100)
    private String name;

    @Column
    @Pattern(regexp =  RegexPattern.REGEXP_USER_PHONE_NUM,message = "전화번호는 010-xxxx-xxxx 형식으로 입력하세요.")
    private String phone;
    @Column
    private String instrument;

    @Column
    private String gender;

    @ElementCollection(fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.REMOVE) // Cascade 설정 추가
    private List<String> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column
    private Provider provider;
    public enum MemberStatus{
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면계정"),
        MEMBER_EXIT("회원탈퇴");

        @Getter
        private final String status;

        MemberStatus(String status){
            this.status = status;
        }
    }
    public enum Provider{
        GOOGLE,LOCAL
    }
}
