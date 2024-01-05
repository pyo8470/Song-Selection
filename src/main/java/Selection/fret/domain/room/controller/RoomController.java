package Selection.fret.domain.room.controller;

import Selection.fret.domain.member.dto.MemberDto;
import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.room.dto.RoomDto;
import Selection.fret.domain.room.entity.Room;
import Selection.fret.domain.room.mapper.RoomMapper;
import Selection.fret.domain.room.service.RoomService;
import Selection.fret.global.response.SingleResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Validated
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomMapper roomMapper;
    private final RoomService roomService;
    // 룸 생성
    @PostMapping
    public ResponseEntity postRoom(@Valid @RequestBody RoomDto.Post Post){
        Room room = roomMapper.roomPostDtoToRoom(Post);
        Room createdRoom = roomService.createRoom(room);
        RoomDto.Response response= roomMapper.roomToResponseDto(createdRoom);
        return new ResponseEntity<>(new SingleResponseDto<>(response),HttpStatus.CREATED);
    }
    // 룸을 선택할 시, 그 룸에 대한 자세한 정보들을 프론트로 보내는 역할
    @GetMapping()
    public ResponseEntity getSelectRoom(){
        return new ResponseEntity<>(
                HttpStatus.OK);
    }
    // 룸 목록을 보여주는 역할
    @GetMapping("/{room-id}")
    public ResponseEntity getRooms(){
        return new ResponseEntity<>(
                HttpStatus.OK);
    }

    // 룸 파괴
    @DeleteMapping
    public ResponseEntity deleteRooms(){
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT);
    }

}
