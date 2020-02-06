package hu.gdf.szgd.dishbrary.web.validation;

import hu.gdf.szgd.dishbrary.security.DishbraryUser;
import hu.gdf.szgd.dishbrary.service.exception.DishbraryValidationException;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class DishbraryUserValidationUtil {

	private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

	public static void validateUser(DishbraryUser user, boolean withPasswordCheck) {
		StringBuilder errorMessageBuilder = null;

		if (!StringUtils.hasText(user.getUsername())) {
			errorMessageBuilder = initOrAppendMessageInNewLine(errorMessageBuilder, "A felhasználónév kitöltése kötelező!");
		}

		if (!StringUtils.hasText(user.getEmail())) {
			errorMessageBuilder = initOrAppendMessageInNewLine(errorMessageBuilder, "Az email cím kitöltése kötelező!");
		} else if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
			errorMessageBuilder = initOrAppendMessageInNewLine(errorMessageBuilder, "Az email cím nem megfelelő formátumú!");
		}

		if (withPasswordCheck && !StringUtils.hasText(user.getPassword())) {
			errorMessageBuilder = initOrAppendMessageInNewLine(errorMessageBuilder, "A jelszó kitöltése kötelező!");
		}

		if (errorMessageBuilder != null) {
			throw new DishbraryValidationException(errorMessageBuilder.toString());
		}
	}

	private static StringBuilder initOrAppendMessageInNewLine(StringBuilder sb, String message) {
		if (sb == null) {
			return new StringBuilder(message);
		}

		return sb.append("\n").append(message);
	}
}

