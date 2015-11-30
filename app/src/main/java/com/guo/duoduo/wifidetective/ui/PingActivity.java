package com.guo.duoduo.wifidetective.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.guo.duoduo.wifidetective.R;
import com.guo.duoduo.wifidetective.util.NetworkUtil;
import com.guo.duoduo.wifidetective.util.ToastUtils;


public class PingActivity extends Activity
{

    private TextView mPingResult;
    private EditText mPingEdit;
    private String mPingIp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);

        mPingResult = (TextView) findViewById(R.id.ping_result);
        mPingEdit = (EditText) findViewById(R.id.edit_input);
        mPingEdit.requestFocus();
        mPingEdit.setFocusable(true);

        //显示软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(mPingEdit.getWindowToken(), 0);
    }

    public void doBtnAction(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_ping :
                mPingIp = mPingEdit.getText().toString();
                if (TextUtils.isEmpty(mPingIp))
                {
                    ToastUtils.showTextToast(this, getString(R.string.input_ip_please));
                    return;
                }
                else
                {
                    new Thread()
                    {
                        public void run()
                        {
                            final String result = NetworkUtil.ping(mPingIp, "10");
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (TextUtils.isEmpty(result))
                                        mPingResult
                                                .setText(getString(R.string.ping_fail));
                                    else
                                        mPingResult.setText(result);
                                }
                            });
                        }
                    }.start();
                }
                break;
        }
    }
}
