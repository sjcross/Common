package wbif.sjx.common.Exceptions;

public class LongOverflowException extends RuntimeException {
    public LongOverflowException(String errorMessage) {
        super(errorMessage);
    }
}
