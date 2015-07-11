
package com.jiezhi.jwc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jiezhi.lib.activity.R;

public class JwcFrame extends Activity {

    private final String TAG = JwcFrame.class.getSimpleName();
    private Intent intent;
    private Button jcxxBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jwc_frame);
        intent = getIntent();

        intent.setClass(JwcFrame.this, WebActivity.class);

        jcxxBtn = (Button) findViewById(R.id.jcxx_btn);
        MyButtonListener listener = new MyButtonListener();
        jcxxBtn.setOnClickListener(listener);
    }

    private class MyButtonListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.jcxx_btn:

                    break;

                default:
                    break;
            }
            startActivity(intent);
        }

    }
}
