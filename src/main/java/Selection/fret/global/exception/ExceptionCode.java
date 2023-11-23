package Selection.fret.global.exception;

import lombok.Getter;

public enum ExceptionCode{
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXISTS(409, "회원이 존재합니다"),
    MEMBER_IS_SLEEPING(404,"휴면 계정입니다"),
    MEMBER_IS_DELETED(404, "탈퇴한 회원입니다");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
