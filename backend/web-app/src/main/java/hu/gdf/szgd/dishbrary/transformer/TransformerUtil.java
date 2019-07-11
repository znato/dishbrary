package hu.gdf.szgd.dishbrary.transformer;

public class TransformerUtil {

	private static final long MILLIS_IN_MINUTE = 60000;

	public static Long minuteToMillis(Integer minute) {
		if (minute == null) {
			return null;
		}

		return minute * MILLIS_IN_MINUTE;
	}
}
