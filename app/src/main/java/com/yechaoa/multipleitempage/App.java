package com.yechaoa.multipleitempage;

import android.app.Application;

import com.yechaoa.yutils.ActivityUtil;
import com.yechaoa.yutils.LogUtil;
import com.yechaoa.yutils.YUtils;

/**
 * Created by yechao on 2017/12/16.
 * Describe :
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        YUtils.initialize(this);
        LogUtil.setIsLog(true);
        registerActivityLifecycleCallbacks(ActivityUtil.getActivityLifecycleCallbacks());

    }

}
