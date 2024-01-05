package Selection.fret.global.regexp;

public class RegexPattern {
    // 비밀번호 제약조건
    public static final String REGEXP_USER_PW_TYPE = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{10,128}+$";
    // 전화번호 제약조건
    public static final String REGEXP_USER_PHONE_NUM = "^010-\\d{4}-\\d{4}$";
}
