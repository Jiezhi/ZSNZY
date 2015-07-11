
package com.jiezhi.jwc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.jiezhi.lib.activity.R;
import com.umeng.analytics.MobclickAgent;

public class WebActivity extends Activity {

    private final String TAG = WebActivity.class.getSimpleName();

    private String html;
    private WebView myWebView;
    private WebView myWebView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        myWebView = (WebView) findViewById(R.id.webView1);
        myWebView2 = (WebView) findViewById(R.id.webView2);
        Intent i = this.getIntent();
        html = i.getStringExtra("url");

        // int time = Integer.valueOf(i.getStringExtra("refreshTime"));
        Log.d(TAG, "html---->" + html);
        myWebView.loadUrl(html);

        try {
            new Thread().sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myWebView2.loadUrl("http://202.195.210.227/bxqcjcxAction.do");

        // myWebView.loadUrl("http://202.195.210.227/logout.do");
        // html = "http://202.195.210.227/bxqcjcxAction.do?JSESSIONID="
        // + GlobleData.cookies.get(0).getValue();
        // System.out.println(html);
        // myWebView.loadUrl(html);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            myWebView.loadUrl("http://202.195.210.227/logout.do");
            this.finish();
        }
        return false;
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
