package com.jiezhi.lib.util;

import java.util.List;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UrlUtil {
	public static final String MAIN_SEARCH_URL = "http://lib.njutcm.edu.cn:8088/opac/search.php";
	public static final String LOGIN_URL = "http://lib.njutcm.edu.cn:8088/reader/redr_verify.php";
	public static final String BOOK_LST = "http://lib.njutcm.edu.cn:8088/reader/book_lst.php";
	public static final String MAIN_URL = "http://lib.njutcm.edu.cn:8088/opac/openlink.php";
	public static final String PREDETAIL_URL = "http://lib.njutcm.edu.cn:8088/opac/";
	public static final String NEWS_URL = "http://lib.njutcm.edu.cn/site/listXiaoxi.jsp";
	// public static final String LOGIN_URL =
	// "http://10.90.0.15:8088/reader/redr_verify.php";
	// public static final String BOOK_LST =
	// "http://10.90.0.15:8088/reader/book_lst.php";
	// public static final String MAIN_URL =
	// "http://10.90.0.15:8088/opac/openlink.php";

	public static final String JWC_URL = "http://202.195.210.227/loginAction.do?zjh1=&tips=&lx=&evalue=&eflag=&fs=&dzslh=&zjh=";
	// 判断登录状态
	public static Boolean flag = false;

	public static List<Cookie> cookies;

	public static boolean hasInternet(Activity a) {
		ConnectivityManager manager = (ConnectivityManager) a
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}
}
