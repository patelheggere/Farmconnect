package com.patelheggere.farmconnect.activity.splash;

import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import com.patelheggere.farmconnect.R;
import com.patelheggere.farmconnect.activity.languegae.LanguageActivity;
import com.patelheggere.farmconnect.activity.farmermain.MainActivity;
import com.patelheggere.farmconnect.activity.merchantmain.MerchantMainActivity;
import com.patelheggere.farmconnect.base.BaseActivity;
import com.patelheggere.farmconnect.utils.SharedPrefsHelper;

import static com.patelheggere.farmconnect.utils.AppUtils.Constants.FIRST_TIME;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.LOGIN_TYPE;
import static com.patelheggere.farmconnect.utils.AppUtils.Constants.THREE_SECOND;


public class Splash2Activity extends BaseActivity {

    @Override
    protected int getContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash2;
    }

    @Override
    protected void initView() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(SharedPrefsHelper.getInstance().get(FIRST_TIME, true)) {
                    Intent i = new Intent(Splash2Activity.this, LanguageActivity.class);
                    startActivity(i);
                }
                else {
                    boolean isFarmer = SharedPrefsHelper.getInstance().get(LOGIN_TYPE);
                    Intent i;
                    if(isFarmer) {
                        i = new Intent(Splash2Activity.this, MainActivity.class);
                    }
                    else
                    {
                        i = new Intent(Splash2Activity.this, MerchantMainActivity.class);
                    }
                    startActivity(i);
                }

                // close this activity
                finish();

            }



        }, THREE_SECOND);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
