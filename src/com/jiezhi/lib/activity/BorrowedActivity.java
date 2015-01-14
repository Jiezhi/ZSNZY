package com.jiezhi.lib.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.jiezhi.lib.util.JsoupUtil;
import com.jiezhi.lib.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class BorrowedActivity extends Activity {
	private ListView list;
	private Button button;
	private ProgressDialog mypDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrowed);

		mypDialog = new ProgressDialog(BorrowedActivity.this);
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mypDialog.setTitle("请稍等");
		mypDialog.setMessage("正在查找...");
		mypDialog.setIndeterminate(false);
		mypDialog.setCancelable(false);

		list = (ListView) findViewById(R.id.borrowed_section_list);

		new BorrowedBook().execute();
	}

	class BorrowedBook extends
			AsyncTask<String, Integer, List<Map<String, Object>>> {

		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			// TODO Auto-generated method stub

			return JsoupUtil.getBorrowedBook();
		}

		@Override
		protected void onPostExecute(final List<Map<String, Object>> result) {
			// TODO Auto-generated method stub
			System.out.println(result);
			mypDialog.cancel();
			if (result == null) {
				ToastUtil.showToast(getApplicationContext(), "您当前没有借阅，或者出错了！");
				finish();
			} else {
				SimpleAdapter listAdapter = new SimpleAdapter(
						getApplicationContext(), result,
						R.layout.borrowed_list, new String[] { "barcode",
								"booktitle", "borrowedDate", "paybackDate" },
						new int[] { R.id.barcodeTV, R.id.bookTitleTV,
								R.id.borrowedDateTV, R.id.paybackDateTV });
				list.setAdapter(listAdapter);
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Map<String, Object> map = result.get(arg2);
						System.out.println(map.get("bookDetail").toString());

						Intent intent = new Intent(getApplicationContext(),
								DetailActivity.class);
						intent.putExtra("detailUrl", map.get("bookDetail")
								.toString());
						intent.putExtra("title", map.get("booktitle")
								.toString());
						startActivity(intent);

					}
				});
			}

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mypDialog.show();
			super.onPreExecute();
		}

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
