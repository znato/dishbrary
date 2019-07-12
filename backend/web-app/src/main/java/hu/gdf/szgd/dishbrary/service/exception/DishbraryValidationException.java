package hu.gdf.szgd.dishbrary.service.exception;

public class DishbraryValidationException extends RuntimeException {

	public DishbraryValidationException() {
	}

	public DishbraryValidationException(String message) {
		super(message);
	}

	public DishbraryValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DishbraryValidationException(Throwable cause) {
		super(cause);
	}

	public DishbraryValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
