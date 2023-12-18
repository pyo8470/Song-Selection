package Selection.fret.domain.song.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Song {
    @Id
    private Long songId;

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public Long getSongId() {
        return songId;
    }
}
