package Selection.fret.test.member;

import Selection.fret.domain.member.controller.MemberController;
import Selection.fret.domain.member.dto.MemberDto;
import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.member.mapper.MemberMapper;
import Selection.fret.domain.member.service.MemberService;
import Selection.fret.global.response.SingleResponseDto;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberController memberController;

    // 제약조건 테스트
    @Test
    public void testPostMemberSuccess() {
        // Arrange
        MemberDto.PostDto postDto = new MemberDto.PostDto();
        // postDto의 속성을 설정하세요
        postDto.setName("김치");
        postDto.setEmail("test@gmail.com");
        Member member = new Member(); // 필요한 경우 Member 객체를 생성하세요
        member.setName("김치");
        member.setEmail("test@gmail.com");
        when(memberMapper.memberPostDtoToMember(postDto)).thenReturn(member);

        Member savedMember = new Member(); // 필요한 경우 저장된 Member 객체를 생성하세요
        savedMember.setName("김치");
        savedMember.setEmail("test@gmail.com");
        when(memberService.createMember(any(Member.class))).thenReturn(savedMember);

        // Act
        ResponseEntity<SingleResponseDto<String>> responseEntity = memberController.postMember(postDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        // 필요에 따라 추가의 검증을 하세요
        assertEquals("김치",savedMember.getName());
        assertEquals("test@gmail.com",savedMember.getEmail());
    }

    @DisplayName("사용자 추가 실패 - 이메일 형식이 아님")
    @Test
    public void addUserFail_NotEmailFormat() throws Exception {
        // given
        final MemberDto.PostDto postDto = new MemberDto.PostDto();

        postDto.setPhone("010");
        postDto.setPassword("111");
        // when

        // then
    }
}