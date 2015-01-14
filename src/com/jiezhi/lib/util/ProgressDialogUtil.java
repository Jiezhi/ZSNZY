package com.jiezhi.lib.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtil {

	private static ProgressDialog mDialog;

	public static void show(Context c) {
		mDialog = new ProgressDialog(c);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setTitle("请稍等");
		mDialog.setMessage("正在查找...");
		mDialog.setIndeterminate(false);
		mDialog.setCancelable(false);

		mDialog.show();
	}

	public static void cancel() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
}
