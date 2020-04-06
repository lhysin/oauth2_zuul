package io.test.exception;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {

    private final ErrorType errorType;
    private final transient Object data;

    public BusinessException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = null;
    }

    public BusinessException(ErrorType errorType, Object data) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = data;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

    public String getErrorCode() {
        return this.errorType.getErrorCode();
    }

    public String getErrorMessage() {
        return this.errorType.getMessage();
    }

    public Object getData() {
        return this.data;
    }
}