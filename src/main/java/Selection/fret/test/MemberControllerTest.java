package Selection.fret.test;

import Selection.fret.domain.member.controller.MemberController;
import Selection.fret.domain.member.dto.MemberDto;
import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.member.mapper.MemberMapper;
import Selection.fret.domain.member.service.MemberService;
import Selection.fret.global.response.SingleResponseDto;
import org.junit.Test;
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

    @Test
    public void testPostMemberPasswordMismatch() {
        // Arrange
        MemberDto.PostDto postDto = new MemberDto.PostDto();
        // postDto의 속성을 설정하세요
        postDto.setPassword("1234");
        postDto.setConformPassword("123");
        // 비밀번호 일치 여부를 true로 설정하여 모의 객체 반환
        when(memberService.validatePassword(postDto.getPassword(), postDto.getConformPassword())).thenReturn(true);

        // Act
        ResponseEntity<SingleResponseDto<String>> responseEntity = memberController.postMember(postDto);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        // 필요에 따라 추가의 검증을 하세요
    }

    // 응용 프로그램의 로직에 따라 더 많은 테스트 케이스를 추가하세요
}