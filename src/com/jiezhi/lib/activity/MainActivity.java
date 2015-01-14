package com.jiezhi.lib.activity;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jiezhi.lib.util.ToastUtil;
import com.jiezhi.lib.util.UrlUtil;
import com.jiezhi.lib.view.LeftSliderLayout;
import com.jiezhi.lib.view.LeftSliderLayout.OnLeftSliderLayoutStateListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.zxing.activity.CaptureActivity;

public class MainActivity extends FinalActivity implements OnClickListener,
		OnLeftSliderLayoutStateListener, OnItemClickListener,
		OnItemSelectedListener {

	private FeedbackAgent agent;

	private Button clearButton;
	private String[] mMenuData;
	private Intent intent;
	private int mMenuWidth;
	// 屏幕宽度
	private int screenWidth;
	private StringBuilder html;
	private String strSearchType;
	private String match_flag;
	private String strText;

	@ViewInject(id = R.id.more_list)
	ListView mMoreList;
	@ViewInject(id = R.id.menuButton)
	ImageButton mMoreBtn;
	@ViewInject(id = R.id.layout_content)
	LeftSliderLayout mLayoutContent;
	@ViewInject(id = R.id.id_left_slider_content)
	View mainView;
	@ViewInject(id = R.id.searchButton)
	ImageButton searchButton;
	@ViewInject(id = R.id.searchText)
	EditText searchText;
	@ViewInject(id = R.id.mainText)
	TextView mainText;
	@ViewInject(id = R.id.spinner_search_type)
	Spinner searchType;
	@ViewInject(id = R.id.spinner_match_flag)
	Spinner mathcFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 友盟自动更新
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);

		agent = new FeedbackAgent(this);
		agent.sync();

		init();
		if (!UrlUtil.hasInternet(MainActivity.this)) {
			ToastUtil.showToast(getApplicationContext(), "网络连接失败，请检查网络！");
		}
		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		mMenuWidth = dip2px(this, px2dip(this, screenWidth / 3));

		mMenuData = getResources().getStringArray(R.array.menu_array);
		MoreAdapter more = new MoreAdapter(this, mMenuData);
		mMoreList.setAdapter(more);
		mMoreList.setOnItemClickListener(this);
		mMoreBtn.setOnClickListener(this);
		mLayoutContent.setOnLeftSliderLayoutListener(this);
		mainText.setText(Html
				.fromHtml("<p><a href='http://3g.renren.com/profile.do?id=333447753'>@耿介之</a>"
						+ "<p><a href='http://weibo.com/xingguangok'>@Jiezhi_G</a>"));
		mainText.setMovementMethod(LinkMovementMethod.getInstance());
		// 更改输入法动作
		searchText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_GO) {
					/*
					 * intent.setClass(getApplicationContext(),
					 * LoginActivity.class); intent.putExtra("info",
					 * "studentInfo"); startActivity(intent);
					 */
					return true;
				} else {
					return false;
				}

			}
		});
		// 监听输入变化
		searchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				if (s.length() > 0) {
					clearButton.setVisibility(View.VISIBLE);
				} else {
					clearButton.setVisibility(View.GONE);
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
		});
		searchText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					try {
						// 字符编码，尤其对中文字符
						strText = new String(searchText.getText().toString()
								.trim().getBytes(), "ISO-8859-1");
						if (strText.equals("")) {

							ToastUtil.showToast(getApplicationContext(),
									"请输入检索内容!");

						} else {

							UrlEncode(strText);
							Intent intent = new Intent();
							intent.setClass(MainActivity.this,
									SearchResultActivity.class);
							intent.putExtra("URL", html.toString());
							startActivity(intent);

						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return false;
			}

		});

		clearButton = (Button) findViewById(R.id.clearBtn);
		clearButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchText.setText("");
			}
		});

		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, CaptureActivity.class);
				startActivityForResult(i, 0);
			}

		});
	}

	protected void UrlEncode(String strText) {
		// TODO Auto-generated method stub
		html = new StringBuilder();
		html.append(UrlUtil.MAIN_URL);
		html.append("?");
		html.append("strText=" + strText);
		html.append("&strSearchType=" + strSearchType);
		html.append("&match_flag=" + match_flag);
		html.append("&showmode=table");
		System.out.println(html.toString());
	}

	private void init() {
		// TODO Auto-generated method stub

		searchType.setOnItemSelectedListener(this);
		mathcFlag.setOnItemSelectedListener(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			strText = bundle.getString("result");
			String tmp = strSearchType;
			strSearchType = "isbn";
			UrlEncode(strText);
			strSearchType = tmp;
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SearchResultActivity.class);
			intent.putExtra("URL", html.toString());
			startActivity(intent);
		}

	}

	// Spinner监听
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.spinner_search_type) {
			switch (arg0.getSelectedItemPosition()) {
			case 0:
				strSearchType = "title";
				break;
			case 1:
				strSearchType = "author";
				break;
			case 2:
				strSearchType = "keyword";
				break;
			case 3:
				strSearchType = "isbn";
				break;
			case 4:
				strSearchType = "asordno";
				break;
			case 5:
				strSearchType = "coden";
				break;
			case 6:
				strSearchType = "callno";
				break;
			case 7:
				strSearchType = "publisher";
				break;
			case 8:
				strSearchType = "series";
				break;
			case 9:
				strSearchType = "tpinyin";
				break;
			case 10:
				strSearchType = "apinyin";
				break;

			default:
				strSearchType = "title";
				break;
			}
			System.out.println(strSearchType);
		} else if (arg0.getId() == R.id.spinner_match_flag) {
			switch (arg0.getSelectedItemPosition()) {
			case 0:
				match_flag = "forward";
				break;
			case 1:
				match_flag = "full";
				break;
			case 2:
				match_flag = "any";
				break;
			default:
				match_flag = "forward";
				break;
			}
			System.out.println(match_flag);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		System.out.println("NothingSelected");
	}

	/**
	 * 处理菜单按钮事件
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String menuClicked = mMoreList.getItemAtPosition(arg2).toString();
		// Toast.makeText(getApplicationContext(), menuClicked, 500).show();
		boolean logined = false;
		if ("个人信息".equals(menuClicked)) {

			if (!logined) {
				intent = new Intent(MainActivity.this, LoginActivity.class);
				intent.putExtra("info", "studentInfo");
				startActivity(intent);
			}
		} else if ("当前借阅".equals(menuClicked)) {
			if (logined) {

			} else {
				intent = new Intent(MainActivity.this, LoginActivity.class);
				intent.putExtra("info", "borrowed");
				startActivity(intent);
			}
		} else if ("意见反馈".equals(menuClicked)) {
			// Toast.makeText(getApplicationContext(), "即将推出，敬请期待！",
			// 500).show();
			agent.startFeedbackActivity();
			// intent = new Intent(MainActivity.this,
			// ConversationActivity.class);
			// startActivity(intent);
		} else if ("分享".equals(menuClicked)) {
			// Toast.makeText(getApplicationContext(), "即将推出，敬请期待！",
			// 500).show();
			intent = new Intent(MainActivity.this, ShareActivity.class);
			startActivity(intent);
		} else if ("成绩查询".equals(menuClicked)) {
			intent = new Intent(MainActivity.this, LoginActivity.class);
			intent.putExtra("info", "jwc");
			startActivity(intent);
		} else if ("退出程序".equals(menuClicked)) {
			finish();
			System.exit(0);
		} else if ("关于软件".equals(menuClicked)) {
			ToastUtil
					.showToast(getApplicationContext(),
							"此软件由耿介之制作，禁止反编译。免费供南中医师生使用，请勿用作商业用途，谢谢支持。(如果您有任何建议，请点击上面的反馈按钮。)");
			// AlertDialog.Builder dialog = new
			// Builder(getApplicationContext());
			// dialog.setTitle("关于");
			// dialog.setMessage("此软件由耿介之制作，禁止反编译。免费供南中医师生使用，请勿用作商业用途，谢谢支持。(如果您有任何建议，请点击上面的反馈按钮。)");
			// dialog.setPositiveButton("", new OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // TODO Auto-generated method stub
			//
			// }
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			//
			// }
			// });
			// dialog.create().show();
		} else if ("更新软件".equals(menuClicked)) {
			UmengUpdateAgent.forceUpdate(this);
		}
	}

	class MoreAdapter extends BaseAdapter {

		private String[] mResultData = {};
		private LayoutInflater mInflater;

		public MoreAdapter(Context pContext, String[] pMenuData) {
			mInflater = LayoutInflater.from(pContext);
			mResultData = pMenuData;
		}

		@Override
		public int getCount() {
			return mResultData.length;
		}

		@Override
		public String getItem(int position) {
			return mResultData[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView mTV = null;
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.more_item, null);
				mTV = (TextView) convertView.findViewById(R.id.title_content);

				convertView.setTag(mTV);
			} else {
				mTV = (TextView) convertView.getTag();
			}
			ImageView mLine = (ImageView) convertView.findViewById(R.id.line);
			mLine.setVisibility(View.GONE);

			mTV.setText(mResultData[position]);
			if (position == 0) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTV
						.getLayoutParams();
				lp.leftMargin = mMenuWidth / 4;
				mTV.setLayoutParams(lp);
				mTV.setTextSize(22);
				convertView.setClickable(true);
			} else {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTV
						.getLayoutParams();
				lp.leftMargin = 20;
				mTV.setLayoutParams(lp);
				mTV.setTextSize(18);
				convertView.setClickable(false);
			}

			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		moreMenuOp();

	}

	/**
	 * 菜单打开与关闭
	 * 
	 * @return
	 */
	boolean moreMenuOp() {
		boolean isOpen = false;
		if (mLayoutContent != null) {
			isOpen = mLayoutContent.isOpen();
			if (isOpen) {
				mLayoutContent.close();
			} else {
				mLayoutContent.open();
			}
		}
		return isOpen;
	}

	@Override
	public void OnLeftSliderLayoutStateChanged(boolean bIsOpen) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean OnLeftSliderLayoutInterceptTouch(MotionEvent ev) {
		if (!mLayoutContent.isOpen()
				&& isViewTouched(mLayoutContent, ev.getRawX(), ev.getRawY())) {

			return true;// 如果是触摸在中间界面，则拦截事件，让中间接管
		}
		return false;
	}

	private boolean isViewTouched(View view, float fX, float fY) {
		int location[] = new int[2];
		view.getLocationOnScreen(location);

		int nStartY = location[1];
		int nEndY = location[1] + view.getHeight();

		int nStartX = location[0];
		int nEndX = location[0] + view.getWidth();

		if ((fY >= nStartY && fY < nEndY) && (fX > nStartX && fX < nEndX)) {
			return true;
		}

		return false;
	}

	/**
	 * px转dip
	 * */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * dip转px
	 * */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
		}
		return false;
	}

	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer timer = null;
		if (!isExit) {
			isExit = true;
			ToastUtil.showToast(getApplicationContext(), "再按一次退出程序");
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					isExit = false;
				}
			}, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

}