package com.jiezhi.lib.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void showToast(Context c, String s) {
		Toast.makeText(c, s, Toast.LENGTH_SHORT).show();

	}
}
