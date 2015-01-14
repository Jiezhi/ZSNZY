package com.jiezhi.lib.activity;

import java.util.Iterator;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;

import org.jsoup.nodes.Element;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiezhi.lib.model.BookInfo_Temp;
import com.jiezhi.lib.util.JsoupUtil;
import com.jiezhi.lib.util.ProgressDialogUtil;
import com.jiezhi.lib.util.ToastUtil;
import com.jiezhi.lib.util.UrlUtil;
import com.umeng.analytics.MobclickAgent;

public class DetailActivity extends FinalActivity {

	private String detailUrl;
	private FinalBitmap fBitmap;
	private StringBuffer sb;

	@ViewInject(id = R.id.titleTV)
	TextView bookTitle;
	@ViewInject(id = R.id.authorTV)
	TextView bookAuthor;
	@ViewInject(id = R.id.book_info)
	TextView book_info;
	@ViewInject(id = R.id.guancang_info)
	TextView guancang_info;
	@ViewInject(id = R.id.bookImage)
	ImageView bookImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_detail);

		fBitmap = FinalBitmap.create(this);
		fBitmap.configLoadingImage(R.drawable.ic_launcher);
		fBitmap.configBitmapMaxHeight(135);
		fBitmap.configBitmapMaxWidth(85);

		Intent intent = this.getIntent();
		detailUrl = UrlUtil.PREDETAIL_URL + intent.getStringExtra("detailUrl");
		bookTitle.setText(intent.getStringExtra("title"));
		// bookAuthor.setText(intent.getStringExtra("author"));
		// fBitmap.display(bookImage,
		// "http://img3.douban.com/mpic/s24502966.jpg");
		new LoadBookDetail().execute(detailUrl);
	}

	class LoadBookDetail extends AsyncTask<String, String, BookInfo_Temp> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			ProgressDialogUtil.show(DetailActivity.this);
		}

		@Override
		protected BookInfo_Temp doInBackground(String... params) {
			// TODO Auto-generated method stub
			return JsoupUtil.loadDetail(params[0]);
		}

		@Override
		protected void onPostExecute(BookInfo_Temp result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ProgressDialogUtil.cancel();
			if (result != null) {
				sb = new StringBuffer();
				Iterator<Element> i = result.getIntroduction().iterator();
				while (i.hasNext()) {
					sb.append(i.next().text() + "\n");
				}
				book_info.setText(sb);
				i = result.getGuancang().iterator();
				sb.delete(0, sb.length());
				while (i.hasNext()) {
					sb.append(i.next().text() + "\n");
				}
				guancang_info.setText(sb);
				System.out.println("ImageURL:" + result.getImageURL());
				fBitmap.display(bookImage, result.getImageURL());
			} else {
				ToastUtil.showToast(getApplicationContext(), "载入失败！");
			}
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
