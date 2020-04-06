package io.test.exception;

public enum ErrorType {

    // @formatter:off
    FAIL_LOGIN("FAIL_LOGIN", "로그인에 실패하였습니다."),
    NOT_MATCH_ADMIN_PWD("NOT_MATCH_ADMIN_PWD", "비밀번호가 일치하지 않습니다."),
    NOT_EXISTS_ID("NOT_EXISTS_ID", "존재하지 않는 ID입니다."),
    
    UNSAFE_FILE_EXTENTION("UNSAFE_FILE", "유효하지 않은 파일 확장자입니다."),
    ACCESS_DENIED_EXCEPTION("ACCESS_DENIED", "권한이 없습니다."),
    BIND_EXCEPTION("BIND_EXCEPTION", "요청 데이터가 잘못되었습니다."),
    UNCATHED_EXCEPTION("FAIL9999", "서버에서 오류가 발생하였습니다.");
    // @formatter:on

    private String errorCode;
    private String message;

    ErrorType(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}