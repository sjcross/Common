package wbif.sjx.common.Exceptions;

public class IntegerOverflowException extends RuntimeException {
    public IntegerOverflowException(String errorMessage) {
        super(errorMessage);
    }
}
