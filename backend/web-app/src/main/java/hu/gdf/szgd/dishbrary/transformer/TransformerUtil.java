package hu.gdf.szgd.dishbrary.transformer;

public class TransformerUtil {

	public static final TransformerConfig TRANSFORMER_CONFIG_FOR_RECIPE_PREVIEW =
			TransformerConfig.includeOnlyFields("id", "name", "creationDate", "owner", "coverImageFileName", "calorieInfo", "editable", "likeable", "favourite");

	private static final long MILLIS_IN_MINUTE = 60000;

	public static Long minuteToMillis(Integer minute) {
		if (minute == null) {
			return null;
		}

		return minute * MILLIS_IN_MINUTE;
	}

	public static Integer millisToMinute(Long millis) {
		if (millis == null) {
			return null;
		}

		return (int) (millis / MILLIS_IN_MINUTE);
	}
}
