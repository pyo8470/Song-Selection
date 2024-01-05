package Selection.fret.domain.room.entity;

import Selection.fret.domain.member.entity.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "room_member")
public class RoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // Constructors, getters, setters, etc.
}
