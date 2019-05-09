package hu.gdf.szgd.dishbrary.service.exception;

public class ConfigurationErrorException extends RuntimeException {
	public ConfigurationErrorException() {
	}

	public ConfigurationErrorException(String message) {
		super(message);
	}

	public ConfigurationErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationErrorException(Throwable cause) {
		super(cause);
	}

	public ConfigurationErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
