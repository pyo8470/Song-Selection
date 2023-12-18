package Selection.fret.domain.room.entity;

import Selection.fret.domain.member.entity.Member;
import Selection.fret.domain.song.entity.Song;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int roomId;

    @Column(name = "room_name", length = 100, nullable = false)
    private String roomName;

    @Column(name = "creator_id", length = 50, nullable = false, unique = true)
    private String creatorId;

    @Column(name = "creator_username", length = 50, nullable = false)
    private String creatorUsername;

    @OneToMany(mappedBy = "room")
    private List<Song> songs;

    @OneToMany(mappedBy = "room")
    private List<Member> members;
}
