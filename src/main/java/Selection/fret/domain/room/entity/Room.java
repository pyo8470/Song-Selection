package Selection.fret.domain.room.entity;

import Selection.fret.domain.audit.Auditable;
import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.song.entity.Song;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Room extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int roomId;

    @Column(name = "room_name", length = 100, nullable = false)
    private String roomName;

    @Column(name = "creator_id", length = 50, nullable = false)
    private Long creatorId;

    @Column(name = "creator_username", length = 50, nullable = false)
    private String creatorUsername;

    
    @OneToMany(mappedBy = "room")
    private List<RoomMember> roomMembers;

    // 투표 컬럼은 다대다 관계가 아님
}
