package cn.sjjy.edu.auth.springmvc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class SolarUtils {
	private static final int COOKIE_AGE = 60 * 60 * 24 * 30;
	public static String LOGGEDIN_USER = "loggedin_user";
	public static final String USER_CONTEXT_COOKIE_KEY = "USER_INFO";

	public static SolarInfo getSolarInfo(HttpServletRequest request) {
		return (SolarInfo) request.getSession().getAttribute(LOGGEDIN_USER);
	}

	public static boolean authPoint(HttpServletRequest request, String point) {
		SolarInfo solarInfo = getSolarInfo(request);
		if (solarInfo == null) {
			return false;
		}

		return solarInfo.getPoints().contains(point);
	}

	public static boolean authPoints(HttpServletRequest request, String... points) {
		SolarInfo solarInfo = getSolarInfo(request);
		if (solarInfo == null) {
			return false;
		}

		for (String point : points) {
			if (!solarInfo.getPoints().contains(point)) {
				return false;
			}
		}
		return true;
	}

	public static void buildRootCookie(HttpServletResponse response, SolarInfo user) {
		JSONObject userJson = JSON.parseObject(JSON.toJSONString(user));
		Cookie cookie = new Cookie(USER_CONTEXT_COOKIE_KEY, userJson.toJSONString());
		cookie.setPath("/");
		cookie.setMaxAge(COOKIE_AGE);
		response.addCookie(cookie);
	}
	
}
