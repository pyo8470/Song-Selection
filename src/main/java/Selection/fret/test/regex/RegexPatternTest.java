package Selection.fret.test.regex;


import Selection.fret.domain.member.dto.MemberDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertFalse;

public class RegexPatternTest {
    private Validator validator;
    private MemberDto.PostDto postDto;
    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
       postDto= MemberDto.PostDto.builder()
                .email("rks1wlska789@naver.com")
                .name("이름")
                .phone("010-1111-")
                .password("1111111")
               .instrument("베이스")
                .gender("남").build();
    }

    @Test
    @DisplayName("전화번호 유효성 테스트")
    public void validatePhone() {
        // given

        // when
        Set<ConstraintViolation<MemberDto.PostDto>> violations = validator.validate(postDto);

        // then
        assertFalse(violations.isEmpty());

        String expectedMessage = "전화번호는 010-xxxx-xxxx 형식으로 입력하세요.";
        for (ConstraintViolation<MemberDto.PostDto> violation : violations) {
            if ("phone".equals(violation.getPropertyPath().toString())) {
                assertThat(violation.getMessage(), containsString(expectedMessage));
            }
        }
    }

    @Test
    @DisplayName("비밀번호 유효성 테스트")
    public void validatePassword() {
        // given
        // when
        Set<ConstraintViolation<MemberDto.PostDto>> violations = validator.validate(postDto);

        // then
        assertFalse(violations.isEmpty());

        String expectedMessage = "비밀번호는 영어,숫자,특수문자 조합입니다.";
        for (ConstraintViolation<MemberDto.PostDto> violation : violations) {
            if ("password".equals(violation.getPropertyPath().toString())) {
                assertThat(violation.getMessage(), containsString(expectedMessage));
            }
        }
//        assertThat(violations,isIn(expectedMessage));
    }
}
