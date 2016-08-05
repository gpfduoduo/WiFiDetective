package com.guo.duoduo.wifidetective.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by 郭攀峰 on 2015/10/26.
 */
public class BaseActivity extends AppCompatActivity {
    private InputMethodManager mInputManager;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void showInput(final View view) {
        if (view == null) return;
        initInputManager();
        view.requestFocus();
        view.postDelayed(new Runnable() {
            @Override public void run() {
                mInputManager.showSoftInput(view,
                        InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }


    protected void hideInput(View view) {
        if (view == null) {
            return;
        }
        initInputManager();
        mInputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void initInputManager() {
        if (mInputManager == null) {
            mInputManager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
        }
    }
}