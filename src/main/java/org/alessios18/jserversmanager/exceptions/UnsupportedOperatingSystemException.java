/**
 *
 */
package org.alessios18.jserversmanager.exceptions;

/** @author alessio */
public class UnsupportedOperatingSystemException extends Exception {

    /** */
    private static final long serialVersionUID = -737580437088062764L;

    public UnsupportedOperatingSystemException() {
        super("This OS is not supported.");
        // TODO Auto-generated constructor stub
    }

    public UnsupportedOperatingSystemException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public UnsupportedOperatingSystemException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public UnsupportedOperatingSystemException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public UnsupportedOperatingSystemException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
