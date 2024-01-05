package Selection.fret.domain.member.dto;

import Selection.fret.domain.member.entity.Member;
import Selection.fret.global.regexp.RegexPattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
public class MemberDto {
    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDto{
        @NotBlank
        private String name;

        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String gender;

        @NotBlank
        private String instrument;

        @Pattern(regexp =  RegexPattern.REGEXP_USER_PHONE_NUM,message = "전화번호는 010-xxxx-xxxx 형식으로 입력하세요.")
        private String phone;
        @NotBlank
        @Pattern(regexp = RegexPattern.REGEXP_USER_PW_TYPE, message = "비밀번호는 영어,숫자,특수문자 조합입니다.")
        private String password;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchDto{

        private String instrument;
        @Pattern(regexp =  RegexPattern.REGEXP_USER_PHONE_NUM,message = "전화번호는 010-xxxx-xxxx 형식으로 입력하세요.")
        private String phone;
        @Pattern(regexp = RegexPattern.REGEXP_USER_PW_TYPE, message = "비밀번호는 영어,숫자,특수문자 조합입니다.")
        private String password;
        @Pattern(regexp = RegexPattern.REGEXP_USER_PW_TYPE, message = "비밀번호는 영어,숫자,특수문자 조합입니다.")
        private String conformPassword;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto{
        private long userId;
        private String name;
        private String email;
        private String gender;
        private String instrument;
        @Pattern(regexp =  RegexPattern.REGEXP_USER_PHONE_NUM,message = "전화번호는 010-xxxx-xxxx 형식으로 입력하세요.")
        private String phone;
        private Member.MemberStatus memberStatus;
        private String createdAt;
        private String modifiedAt;
    }
}
