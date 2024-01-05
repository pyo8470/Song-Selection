package Selection.fret.domain.room.service;

import Selection.fret.domain.room.entity.Room;
import Selection.fret.domain.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(Room room){
        room.setRoomMembers(null);
        room.setCreatorUsername("방장");
        room.setCreatorId(1L);
        return roomRepository.save(room);
    }
}
