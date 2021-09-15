package com.tencent.mtt.tabcandroidsdk;

import android.app.Application;

import com.tencent.mtt.tabcsdk.ABTestApi;
import com.tencent.mtt.tabcsdk.entity.ABTestConfig;

/**
 * @Date: 2019/6/11
 * @Description: self application
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        UserAction.initUserAction(getApplicationContext());

        ABTestConfig abTestConfig = new ABTestConfig();
        abTestConfig.setUserId("1223");
        abTestConfig.setEnv(ABTestConfig.ENV_DEBUG);

        ABTestApi.init(this, abTestConfig);
    }
}
