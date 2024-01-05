package Selection.fret.domain.member.controller;

import Selection.fret.domain.member.dto.MemberDto;
import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.member.mapper.MemberMapper;
import Selection.fret.domain.member.service.MemberService;
import Selection.fret.global.response.SingleResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    @PostMapping("/signup")
    public ResponseEntity<SingleResponseDto> postMember(@Valid @RequestBody MemberDto.PostDto postDto){

        Member member = memberMapper.memberPostDtoToMember(postDto);
        Member savedMember = memberService.createMember(member);
        MemberDto.ResponseDto responseDto = memberMapper.memberToResponseDto(savedMember);
        return new ResponseEntity<>
                (new SingleResponseDto<>(responseDto),HttpStatus.CREATED);
    }
}
