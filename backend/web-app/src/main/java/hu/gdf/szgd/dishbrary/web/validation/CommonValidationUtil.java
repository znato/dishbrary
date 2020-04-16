package hu.gdf.szgd.dishbrary.web.validation;

import org.owasp.html.HtmlChangeListener;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;

public class CommonValidationUtil {

	private static final PolicyFactory POLICY_FACTORY = new HtmlPolicyBuilder()
			.allowCommonInlineFormattingElements()
			.allowCommonBlockElements().toFactory();

	public static void validateAgainstXSSAttack(String text, RuntimeException throwIfInvalid) {
		if (StringUtils.hasText(text)) {
			POLICY_FACTORY.sanitize(text, new ExceptionThrowerHtmlChangeListener(throwIfInvalid), "");
		}
	}

	private static final class ExceptionThrowerHtmlChangeListener implements HtmlChangeListener<String> {

		private RuntimeException exception;

		public ExceptionThrowerHtmlChangeListener(RuntimeException exception) {
			this.exception = exception;
		}

		@Override
		public void discardedTag(@Nullable String s, String s2) {
			if (exception != null) {
				throw exception;
			}
		}

		@Override
		public void discardedAttributes(@Nullable String s, String s2, String... strings) {
			if (exception != null) {
				throw exception;
			}
		}
	}
}
