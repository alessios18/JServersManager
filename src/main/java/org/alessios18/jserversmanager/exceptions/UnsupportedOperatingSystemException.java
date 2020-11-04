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
	 }

	 public UnsupportedOperatingSystemException(
				String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		  super(message, cause, enableSuppression, writableStackTrace);
	 }

	 public UnsupportedOperatingSystemException(String message, Throwable cause) {
		  super(message, cause);
	 }

	 public UnsupportedOperatingSystemException(String message) {
		  super(message);
	 }

	 public UnsupportedOperatingSystemException(Throwable cause) {
		  super(cause);
	 }
}
