package Selection.fret.domain.room.controller;

import Selection.fret.domain.member.dto.MemberDto;
import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.room.dto.RoomDto;
import Selection.fret.global.response.SingleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@Validated
@RequiredArgsConstructor
@Slf4j
public class RoomController {
    // 룸 생성
    @PostMapping("/signup")
    public ResponseEntity postMember(@Valid @RequestBody RoomDto.Post Post){

        return new ResponseEntity<>(
                HttpStatus.CREATED);
    }
    // 룸 파괴
}
