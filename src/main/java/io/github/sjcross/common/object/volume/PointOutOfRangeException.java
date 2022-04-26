package io.github.sjcross.common.object.volume;

public class PointOutOfRangeException extends Throwable {
    /**
     *
     */
    private static final long serialVersionUID = -7459366989268348241L;

    public PointOutOfRangeException() {
    }

    public PointOutOfRangeException(String message) {
        super(message);
    }

    public PointOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PointOutOfRangeException(Throwable cause) {
        super(cause);
    }

    public PointOutOfRangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
