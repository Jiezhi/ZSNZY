
package com.jiezhi.lib.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jiezhi.jwc.activity.JwcFrame;
import com.jiezhi.lib.model.StudentInfo;
import com.jiezhi.lib.util.JsoupUtil;
import com.jiezhi.lib.util.ToastUtil;
import com.jiezhi.lib.util.UrlUtil;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalDb;

public class LoginActivity extends Activity {

    private final String TAG = LoginActivity.class.getSimpleName();

    private Button loginButton;
    private EditText loginNumber;
    private EditText loginKey;
    private CheckBox recd;
    private ProgressDialog mypDialog;
    private Class<?> cls = StudentInfoActivity.class;
    private String keyFlag = "lib";// 标志保存为图书馆密码还是教务处密码
    private String hint;
    private TextView login_hint;
    private EditText refreshTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        refreshTime = (EditText) findViewById(R.id.refresh_time);
        loginNumber = (EditText) findViewById(R.id.loginNumber);
        loginKey = (EditText) findViewById(R.id.loginKey);
        login_hint = (TextView) findViewById(R.id.login_hint);
        String info = this.getIntent().getStringExtra("info");
        if (info.equals("borrowed")) {
            cls = BorrowedActivity.class;
        } else if (info.equals("studentInfo")) {
            cls = StudentInfoActivity.class;
        } else if (info.equals("jwc")) {

            // cls = WebActivity.class;
            cls = JwcFrame.class;

            // 用户自定义刷新时间，默认不显示
            // findViewById(R.id.refresh_hint).setVisibility(View.VISIBLE);
            // refreshTime.setVisibility(View.VISIBLE);

            keyFlag = "jwc";
            loginKey.setHint("输入教务处账号的密码");
            login_hint.setText(getResources().getString(R.string.hello));
        }
        mypDialog = new ProgressDialog(LoginActivity.this);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mypDialog.setTitle("登录");
        mypDialog.setMessage("登录中...");
        mypDialog.setIndeterminate(false);
        mypDialog.setCancelable(false);

        loginNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                loginKey.setText("");
            }
        });

        recd = (CheckBox) findViewById(R.id.recd);
        // 记住密码
        SharedPreferences pre = getSharedPreferences(keyFlag, MODE_APPEND);
        String user = pre.getString("number", "");
        String pass = pre.getString("passwd", "");
        if (user != "") {
            loginNumber.setText(user);
            loginKey.setText(pass);
            recd.setChecked(true);
        }

        loginButton = (Button) findViewById(R.id.loginButton);
        // 登录
        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String number = loginNumber.getText().toString();
                String passwd = loginKey.getText().toString();
                if (recd.isChecked()) {
                    SharedPreferences pre = getSharedPreferences(keyFlag,
                            MODE_APPEND);
                    Editor edit = pre.edit();
                    edit.putString("number", number);
                    edit.putString("passwd", passwd);
                    // edit.putString("cookie", l.getCookie());
                    edit.commit();

                } else {
                    SharedPreferences sp = getSharedPreferences(keyFlag,
                            MODE_APPEND);
                    sp.edit().clear();
                }

                if (keyFlag.equals("lib")) {
                    new LoginLib().execute(number, passwd);
                } else if (keyFlag.equals("jwc")) {
                    new LoginJwc().execute(number, passwd);
                }

            }
        });

    }

    // 登录教务处
    class LoginJwc extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String html = UrlUtil.JWC_URL + params[0] + "&mm=" + params[1]
                    + "&v_yzm=";

            Log.d(TAG, "loginUrl------->" + html);

            Intent intent = new Intent();
            intent.putExtra("url", html);

            // intent.putExtra("refreshTime", refreshTime.getText().toString());
            intent.setClass(LoginActivity.this, cls);
            startActivity(intent);

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mypDialog.cancel();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mypDialog.show();
        }

    }

    // 登录图书馆
    class LoginLib extends AsyncTask<String, Integer, StudentInfo> {

        @Override
        protected void onPreExecute() {
            mypDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected StudentInfo doInBackground(String... params) {
            return JsoupUtil.loginUrl(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(StudentInfo result) {
            mypDialog.cancel();
            if (result != null) {
                // 已经登录
                Log.d(TAG, "jwc login result---->" + result.toString());
                FinalDb.create(LoginActivity.this, "stu_info").save(result);
                UrlUtil.flag = true;
                Intent i = new Intent();
                i.setClass(getApplicationContext(), cls);
                startActivity(i);
                finish();
            } else {
                ToastUtil.showToast(getApplicationContext(), "登录失败，请检查账号和密码！");
            }
            super.onPostExecute(result);
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
