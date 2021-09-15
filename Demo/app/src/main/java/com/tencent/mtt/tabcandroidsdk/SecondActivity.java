package com.tencent.mtt.tabcandroidsdk;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mtt.tabcsdk.ABTestApi;
import com.tencent.mtt.tabcsdk.entity.ExpEntity;
import com.tencent.mtt.tabcsdk.listener.GetExperimentListener;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Author: 2019/6/20
 * @Description:
 */
public class SecondActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        final TextView textView = findViewById(R.id.second_textview);

        ABTestApi.getExpByName("group2", new GetExperimentListener() {
            @Override
            public void getExperimentSucceed(List<ExpEntity> experimentList) {
                // 从网络加载到策略，也有可能超时使用本地策略
                ExpEntity expEntity = experimentList.get(0);
                Log.d("abtest", expEntity.getAssignment());
                textView.setText(expEntity.getAssignment());
            }

            @Override
            public void getExperimentFailed(int errorCode, String errMsg) {
                // sdk集成有问题，使用默认的逻辑
                // 正常初始化不会走到这里
                textView.setText("default");

            }
        }, 0);
    }
}
