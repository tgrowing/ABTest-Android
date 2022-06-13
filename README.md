# ABTest-android

## 支持版本：
Android Api>=19

## SDK集成

### 在应用的build.gradle的dependencies中添加依赖：

```
    // 网络库
    implementation("com.squareup.okhttp3:okhttp:4.9.1")

    // ABTest SDK 离线化版本
    implementation files('tabcsdk-release-【SDK对应的版本号】.aar')
```
### 初始化SDK:

（1）在应用的Manifest文件中引入在腾讯云AB实验平台创建实验时生成的appkey。同时记得要在清单中增加网络访问和存储读写权限。

```Android
  <meta-data
            android:name="TabcSDK_appKey"
            android:value="申请的appkey" />
			
  // 开启网络、存储读写权限
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

（2）也可以使用代码初始化appkey

```Android
    ABTestConfig abTestConfig = new ABTestConfig();
    abTestConfig.setAppKey("申请的appkey");
```

（3）在应用的application的onCreate回调方法中进行初始化，实验配置参数可以设置userId、环境（debug测试、release）等属性。

```Android
    ABTestConfig abTestConfig = new ABTestConfig();
    abTestConfig.setUserId("123456789"); // 用户唯一身份id，用于分流
```
（4）初始化SDK

```Android
    ABTestApi.init(this, abTestConfig);
```

注：
    1. 查询appkey
	![image.png#634px #227px](https://tencent-growth-platform-1251316161.cos.ap-beijing.myqcloud.com/sdk/images/abtest_sdk/abtest_ios_step_1.png)

## SDK使用

### 在腾讯云AB实验平台配置实验

### 获取实验数据

1. 通过实验标识获取实验数据，并上报一次实验曝光

```Android
    // 该调用会首先从缓存中获取实验，没有缓存的话则发起一次异步的调用，并在拿到结果后回调
    ABTestApi.getExpByNameWithExpose("具体的实验id",mGetListener);
    mGetListener = new GetExperimentListener() {
        @Override
        public void getExperimentSucceed(List<ExpEntity> romaExpEntities) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < romaExpEntities.size(); i++) {
                ExpEntity entity = romaExpEntities.get(i);
                if ("具体的实验版本".equals(entity.assignment)) {
                    // 实现实验版本A的逻辑
                } else if ("具体的实验版本2".equals(entity.assignment)) {
                    // 实验实验对照版本的逻辑
                } else {
                    // 默认逻辑
                }
            }
        }
    };
```

2. 通过实验标识获取实验数据，不会上报实验曝光

```Android
    // 该调用会首先从缓存中获取实验，没有缓存的话则发起一次异步的调用，并在拿到结果后回调
    ExpEntity exp = ABTestApi.getExpByName("具体的实验id", mGetListener);
    mGetListener = new GetExperimentListener() {
        @Override
        public void getExperimentSucceed(List<ExpEntity> romaExpEntities) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < romaExpEntities.size(); i++) {
                ExpEntity entity = romaExpEntities.get(i);
                if ("具体的实验版本".equals(entity.assignment)) {
                    // 实现实验版本A的逻辑
                } else if ("具体的实验版本2".equals(entity.assignment)) {
                    // 实验实验对照版本的逻辑
                } else {
                    // 默认逻辑
                }
            }
        }
    };
```
注：
    1. 查询实验id
![image.png#600px #157px](https://tencent-growth-platform-1251316161.cos.ap-beijing.myqcloud.com/sdk/images/abtest_sdk/abtest_ios_step_2.png)
    2. 查询实验版本
![image.png#600px #285px](https://tencent-growth-platform-1251316161.cos.ap-beijing.myqcloud.com/sdk/images/abtest_sdk/abtest_ios_step_3.png)
### 上报实验相关事件

1. 上报实验曝光
```Android
    ABTestApi.reportExpExpose(ExpEntity expEntity);
```

2. 上报实验反馈事件
```Android
    ABTestApi.reportExpEventCode("具体的实验事件code", ExpEntity expEntity)
```

注：
1.  查询实验事件code：
![image.png#600px #179px](https://tencent-growth-platform-1251316161.cos.ap-beijing.myqcloud.com/sdk/images/abtest_sdk/abtest_ios_step_4.png)

