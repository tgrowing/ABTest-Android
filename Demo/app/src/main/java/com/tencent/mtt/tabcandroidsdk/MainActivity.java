package com.tencent.mtt.tabcandroidsdk;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mtt.tabcsdk.ABTestApi;
import com.tencent.mtt.tabcsdk.BuildConfig;
import com.tencent.mtt.tabcsdk.entity.ExpEntity;
import com.tencent.mtt.tabcsdk.listener.GetExperimentListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ABTestManager";
    private static final boolean DEBUG = BuildConfig.DEBUG;

    private TextView dataTv, reportTv, oldTv, tvContent;
    private Button getExpBtn, getExpExposeBtn, getAllExpsBtn, switchAccountId,
            reportBtn, reportExpEventBtn, getAllIdsBtn, isFirstBtn;

    private EditText expNameEt, grayIdEt, eventCodeEt, userIdEt;

    private String expName, expGrayId, userId, eventCode;

    private GetExperimentListener mGetListener, mPoseListener, mExpPosedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

        getExpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expName = expNameEt.getText().toString();
                if (expName.isEmpty()) {
                    expNameEt.setHint("实验名称不为空，请输入实验名称....");
                    Toast.makeText(MainActivity.this, "实验名称不为空，请输入实验名称....",
                            Toast.LENGTH_LONG).show();
                } else {
                    ABTestApi.getExpByName(expName, mGetListener, -1);
                }
            }
        });


        getExpExposeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expName = expNameEt.getText().toString();
                if (expName.isEmpty()) {
                    expNameEt.setHint("实验名称不为空，请输入实验名称....");
                    Toast.makeText(MainActivity.this, "实验名称不为空，请输入实验名称....",
                            Toast.LENGTH_LONG).show();
                } else {
                    ABTestApi.getExpByNameWithExpose(expName, mPoseListener, -1);
                }

            }
        });

        getAllExpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               ABTestApi.getAllExperiments(mGetListener, -1);

            }
        });

        switchAccountId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = userIdEt.getText().toString();
                if (TextUtils.isEmpty(userId)) {
                    userIdEt.setHint("切换用户ID不为空，请重新输入...");
                    Toast.makeText(MainActivity.this, "切换用户ID不为空，请重新输入...",
                            Toast.LENGTH_LONG).show();
                }

                ABTestApi.switchAccountId(userId, mGetListener);
//
            }
        });


        getAllIdsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> expIds = ABTestApi.getAllExpIds();
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText(expIds.toString());
            }
        });


        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取entity,然后上报
                expName = expNameEt.getText().toString();
                if (expName.isEmpty()) {
                    expNameEt.setHint("实验名称不为空，请输入实验名称....");
                    Toast.makeText(MainActivity.this, "实验名称不为空，请输入实验名称....",
                            Toast.LENGTH_LONG).show();
                } else {
                    eventCode = eventCodeEt.getText().toString();
                    ABTestApi.getExpByNameWithExpose(expName, mExpPosedListener, -1);
                }

            }
        });
    }

    private void initView() {

        dataTv = findViewById(R.id.tv_data_interface);
        getExpBtn = findViewById(R.id.bt_ua_1);
        getExpExposeBtn = findViewById(R.id.bt_ua_2);
        getAllExpsBtn = findViewById(R.id.bt_ua_3);
        switchAccountId = findViewById(R.id.bt_ua_4);

        reportTv = findViewById(R.id.tv_report_interface);
        reportBtn = findViewById(R.id.bt_report_1);

        oldTv = findViewById(R.id.tv_old_interface);
        getAllIdsBtn = findViewById(R.id.bt_old_1);
        isFirstBtn = findViewById(R.id.bt_old_2);

        tvContent = findViewById(R.id.tv_content);

        grayIdEt = findViewById(R.id.et_gray_id_value);
        expNameEt = findViewById(R.id.et_param_value);
        eventCodeEt = findViewById(R.id.et_event_code_value);
        userIdEt = findViewById(R.id.et_param_user_id);
    }

    private void initListener() {
        mGetListener = new GetExperimentListener() {
            @Override
            public void getExperimentSucceed(List<ExpEntity> romaExpEntities) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i<romaExpEntities.size(); i++ ) {
                    ExpEntity entity = romaExpEntities.get(i);
                    if (TextUtils.isEmpty(entity.getExpName())) {
                        continue;
                    }
                    builder.append(entity + "\n");
                }
                Log.d(TAG, "mGetListener getExperimentSucceed: " +  builder.toString());
                tvContent.setText("实验数据：\n" + builder.toString());
                tvContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void getExperimentFailed(int errorCode, String errMsg) {
                Log.d(TAG, "onCreate getExperimentFailed: " + errorCode + " " + errMsg);
                tvContent.setText("errorCode: " + errorCode + "  errMsg: " + errMsg);
                tvContent.setVisibility(View.VISIBLE);
            }
        };

        mPoseListener = new GetExperimentListener() {
            @Override
            public void getExperimentSucceed(List<ExpEntity> romaExpEntities) {
                String romaExpsStr = romaExpEntities.toString();
                Log.d(TAG, "mPoseListener getExperimentSucceed: " +  romaExpsStr);
                if (!romaExpEntities.isEmpty()) {
                    ExpEntity entity = romaExpEntities.get(0);
                    boolean isDefault = entity.isDefaultRomaExp();
                    if (isDefault) {
                        tvContent.setText("获取默认的实验数据，曝光失败！\n实验数据：" + romaExpsStr);
                    } else {
                        tvContent.setText("曝光成功，请在灯塔中查看数据上报结果！\n实验数据：" + romaExpsStr);
                    }
                } else {
                    tvContent.setText("曝光失败，获取实验数据不成功 ！\n实验数据：" + romaExpsStr);
                }
                tvContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void getExperimentFailed(int errorCode, String errMsg) {
                Log.d(TAG, "mPoseListener getExperimentFailed: " + errorCode + " " + errMsg);
                tvContent.setText("曝光失败！\nerrorCode: " + errorCode + "  errMsg: " + errMsg);
                tvContent.setVisibility(View.VISIBLE);
            }
        };


        mExpPosedListener = new GetExperimentListener() {

            @Override
            public void getExperimentSucceed(List<ExpEntity> romaExpEntities) {
                if (!romaExpEntities.isEmpty()) {
                    ExpEntity entity = romaExpEntities.get(0);
                    if (TextUtils.isEmpty(eventCode)) {
                        ABTestApi.reportExpExpose(entity);
                    } else {
                        ABTestApi.reportExpEventCode(eventCode, entity);
                    }
                    tvContent.setText("上报成功，请在平台上查看上报数据！");
                    tvContent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void getExperimentFailed(int errorCode, String errMsg) {
                Log.e(TAG, "getExperimentFailed: errorCode:" + errorCode + "  errMsg:" + errMsg);
            }
        };

    }

}
