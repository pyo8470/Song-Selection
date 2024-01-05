package Selection.fret.domain.song.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    // 노래 제목, Youtube url 등을 포함할 예정
    // Youtube API 이용 예정
    @Id
    private Long songId;

    private String youtubeUrl;
}
