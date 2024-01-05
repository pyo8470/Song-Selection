package Selection.fret.domain.room.dto;

import lombok.*;

public class RoomDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post{
        private String roomName;
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    public static class Patch {
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long roomId;
        private String roomName;
        private Long creatorId;
    }
}
