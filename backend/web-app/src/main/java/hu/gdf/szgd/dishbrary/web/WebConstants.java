package hu.gdf.szgd.dishbrary.web;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;

public class WebConstants {
	public static final String REST_ENDPOINT_BASE_PATH = "/rest";
	public static final String JSON_WITH_UTF8_ENCODING = MediaType.APPLICATION_JSON + "; charset=UTF-8";

	public static final CacheControl MAX_AGE_30_MIN;

	static {
		MAX_AGE_30_MIN = new CacheControl();
		MAX_AGE_30_MIN.setMaxAge(30);
	}

}
