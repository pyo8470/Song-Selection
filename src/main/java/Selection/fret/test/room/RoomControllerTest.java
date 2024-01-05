package Selection.fret.test.room;

import Selection.fret.domain.member.controller.MemberController;
import Selection.fret.domain.member.dto.MemberDto;
import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.member.mapper.MemberMapper;
import Selection.fret.domain.member.service.MemberService;
import Selection.fret.domain.room.controller.RoomController;
import Selection.fret.domain.room.dto.RoomDto;
import Selection.fret.domain.room.entity.Room;
import Selection.fret.domain.room.mapper.RoomMapper;
import Selection.fret.domain.room.service.RoomService;
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
public class RoomControllerTest {
    @Mock
    private RoomService roomService;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomController roomController;

    @Test
    public void testPostRoomSuccess() {
        // Arrange
        RoomDto.Post postDto = new RoomDto.Post();
        postDto.setRoomName("방 1");
        Room room = new Room();
        room.setRoomName("방 1");
        when(roomMapper.roomPostDtoToRoom(postDto)).thenReturn(room);

        Room savedRoom = new Room(); //
        savedRoom.setRoomName("방 1");
        when(roomService.createRoom(any(Room.class))).thenReturn(savedRoom);

        // Act
        ResponseEntity responseEntity = roomController.postRoom(postDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        // 필요에 따라 추가의 검증을 하세요
        assertEquals("방 1",savedRoom.getRoomName());
        assertEquals(1, savedRoom.getRoomId());
    }

}
