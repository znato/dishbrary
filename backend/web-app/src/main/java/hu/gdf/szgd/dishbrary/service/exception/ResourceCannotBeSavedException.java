package hu.gdf.szgd.dishbrary.service.exception;

public class ResourceCannotBeSavedException extends RuntimeException {
	public ResourceCannotBeSavedException() {
	}

	public ResourceCannotBeSavedException(String message) {
		super(message);
	}

	public ResourceCannotBeSavedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceCannotBeSavedException(Throwable cause) {
		super(cause);
	}

	public ResourceCannotBeSavedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
