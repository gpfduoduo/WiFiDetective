package com.guo.duoduo.wifidetective.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.ui.view.StatusBarCompat;
import com.guo.duoduo.wifidetective.util.NetworkUtil;
import com.guo.duoduo.wifidetective.util.ToastUtils;

public class PingActivity extends BaseActivity {

    private TextView mPingResult;
    private EditText mPingEdit;
    private String mPingIp;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);

        StatusBarCompat.compat(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ping_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        mPingResult = (TextView) findViewById(R.id.ping_result);
        mPingEdit = (EditText) findViewById(R.id.edit_input);

        showInput(mPingEdit);
    }


    public void doBtnAction(View view) {
        switch (view.getId()) {
            case R.id.btn_ping:
                mPingIp = mPingEdit.getText().toString();
                if (TextUtils.isEmpty(mPingIp)) {
                    ToastUtils.showTextToast(this,
                            getString(R.string.input_ip_please));
                    return;
                }
                else {
                    new Thread() {
                        public void run() {
                            final String result = NetworkUtil.ping(mPingIp,
                                    "10");
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    if (TextUtils.isEmpty(result)) {
                                        mPingResult.setText(
                                                getString(R.string.ping_fail));
                                    }
                                    else {
                                        mPingResult.setText(result);
                                    }
                                }
                            });
                        }
                    }.start();
                }
                break;
        }
    }
}
