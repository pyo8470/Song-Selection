package Selection.fret.domain.member.dto;

import Selection.fret.domain.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

public class MemberDto {
    @NoArgsConstructor
    @Setter
    @Getter
    public static class PostDto{
        @Email
        @NotBlank
        private String email;
        @NotBlank
        private String name;
        @NotBlank
        private String password;
        @NotBlank
        private String conformPassword;
        private String gender;
        private String phone;
        private LocalDate birth;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchDto{
        private String password;
        private String phone;
        private String birth;
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
        private String img;
        private Member.MemberStatus memberStatus;
        private String createdAt;
        private String modifiedAt;
    }
}
