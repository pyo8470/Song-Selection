package Selection.fret.domain.room.mapper;

import Selection.fret.domain.member.dto.MemberDto;
import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.room.dto.RoomDto;
import Selection.fret.domain.room.entity.Room;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface RoomMapper {
    Room roomPostDtoToRoom(RoomDto.Post postDto);

    Room roomPatchDtoToRoom(RoomDto.Patch patchDto);

    RoomDto.Response roomToResponseDto(Room room);
}
