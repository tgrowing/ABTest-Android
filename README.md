tabc_android_sdk
---------------

TABC云实验平台安卓sdk

## SDK集成

### 导入SDK

在dependencies中添加依赖：

```
implementation(group: 'com.tencent.mtt', name: 'tabcsdk-release', version: '1.1.0', ext: 'aar')
```
### 初始化SDK:

（1）在应用的Manifest文件中引入在骡马实验后台创建实验时生成的appkey。同时记得要在清单中增加网络访问和存储读写权限。

```Android
  <meta-data
            android:name="TabcSDK_appKey"
            android:value="申请的appkey" />
```

（2）也可以使用代码初始化appkey

```Android
    ABTestConfig abTestConfig = new ABTestConfig();
    abTestConfig.setAppKey("申请的appkey");
    abTestConfig.setEnv(ABTestConfig.ENV_RELEASE);
```

（3）求在应用的application的onCreate回调方法中进行初始化，实验配置参数可以设置guid、环境（debug测试、release）和标签等属性。

```Android
    ABTestConfig abTestConfig = new ABTestConfig();
    abTestConfig.setGuid("123456789"); // 用于分流的用户身份id

    // 用户属性标签，做标签实验使用。可选
    Map<String,String> profiles = new HashMap<String,String>();
    profiles.put("sexy", "male");
    abTestConfig.setCustomProfiles(profiles);

    // 初始化SDK
    ABTestApi.init(this, abTestConfig);
```

（4）终端主动拉取后台实验的时机当前有四个地方：
    
    sdk初始化时
    
    每一个activity页面创建(onCreate)和销毁(onDestroy)时
    
    调用同步拉取策略接口的时候
    
    定时更新使用策略触发

## SDK使用

### 在TAB实验平台配置实验

### 从本地缓存的策略中获取实验

```Android
    // 通过实验组key获取实验，该API调用会上报一次实验曝光
    ExpEntity exp = ABTestApi.getExpByName("group2");
    if ("treatment".equals(exp.assignment)) {
        // 实验版本treatment的逻辑
    } else if ("control".equals(exp.assignment)) {
        // 实验版本control的逻辑
    } else {
        // 默认逻辑
    }
```

### 同步发起一次后台实验策略请求

```Android
    int timeout = 2; // 请求超时时间为2s
    // 通过实验组key获取实验，该API调用会在后台产生一次实验曝光
    ABTestApi.asyncGetExpByName("group2", new AsyncGetExperimentListener() {
        @Override
        public void getExperimentSucceed(ExpEntity experiment) {
            // 从网络加载到策略，也有可能超时使用本地策略
            if ("treatment".equals(exp.assignment)) {
                // 实验版本treatment的逻辑
            } else if ("control".equals(exp.assignment)) {
                // 实验版本control的逻辑
            } else {
                // 默认逻辑
            }
        }

        @Override
        public void getExperimentFailed(int errorCode, String errMsg) {
            // sdk集成有问题，使用默认的逻辑
            // 正常初始化不会走到这里

            // 默认逻辑
        }
    }, timeout);
```

### 从本地缓存的策略中获取实验，但不产生实验曝光

```Android
    // 通过实验组key获取实验，不会上报实验曝光
    ExpEntity exp = ABTestApi.peekExpByName("group2");
```

### 上报实验相关事件

```Android
    // 上报一条实验曝光
    ABTestApi.reportExpExpose(exp);
    // 上报一条实验关联事件，someAction为在实验平台申请的事件
    ABTestApi.reportExpEventCode("someAction", ExpEntity expEntity)
```

### 其他API

```Android
    // 获取所有命中的实验，key为分层code，value是RomaExp
    Map<String, RomaExp> expMap = ABTestApi.getAllExps();

    // 该接口直接获取后台返回的完整json对象
    JSONObject obj = ABTestApi.getHitExperiment();
    // 该接口通过传入实验id参数返回对应的实验层名称和实验参数值
    JSONObject expObj = ABTestApi.getHitExperiment(grayId);

    // 该接口以列表形式返回命中的所有实验id
    List<String> grayIds = ABTestApi.getExperimentCodes();

    // 该接口通过传入实验id判断是否第一次命中某个实验
    boolean isFirstHit = ABTestApi.isFirstHit(grayId);

    // 通过实验的参数，获取实验的参数值
    Object ret = ABTestApi.getParamsValue(grayId, paramKey, defaultValue);

```

### License
ISC
