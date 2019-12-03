package com.bupt.demo.launcher;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.pushsdk.content.AliPushRcvService;
import com.alipay.pushsdk.data.BDataBean;
import com.alipay.pushsdk.push.PushAppInfo;

public class RecvMsgIntentService extends AliPushRcvService {
    public RecvMsgIntentService() {
        super();
    }

    // 调用时机：
    // 华为、小米手机：接入推送后，点击通知栏时进行调用
    // 其他手机：收到消息时进行调用
    @Override
    protected void handleActionReceived(String s, String s1, boolean clicked) {
        //示例代码
        Intent intent1 = new Intent("tt-action");
        String extra = "key:=" + s + "data:=" + s1;
        intent1.putExtra("intent :", extra);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
        Log.v("push-test", "onHandleIntent sendLocalBroadcast : " + intent1.toString());
        if (TextUtils.isEmpty(s1)) {
            return;
        }
        if(clicked){
            //华为小米采用通知栏消息，所以一定点击过
        }
        BDataBean bDataBean = BDataBean.create(s1);
        // BDataBean 含有自定义的Param
        if (bDataBean == null) {
            return;
        }
        // 处理默认的跳转等事件
        // 可以不调用以下方法，自行处理
        bDataBean.action(this);
    }

    public static String adToken = "";
    public static String thirdToken = "";
    public static int platform = 0;

    // 自建渠道 AdToken 返回
    /** 调用时机：完成初始化后，收到自建渠道标识时调用
     *@param s adtoken 自建渠道标识
     */
    @Override
    protected void handleActionId(String s) {
        // 示例代码
        String registrationId = s;
        PushAppInfo pushAppInfo = new PushAppInfo(getApplicationContext());
        pushAppInfo.setAppToken(registrationId);
        Log.e("push-test", "registrationId = " + registrationId);
        Intent intent1 = new Intent("tt-action");
        String extra = "adToken:=" + s;
        intent1.putExtra("intent :", extra);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
        Log.e("push-test", "onHandleIntent sendLocalBroadcast : " + intent1.toString());
        adToken = s;

    }
    /**
     *调用时机：收到第三方渠道标识时调用
     *@param s thirdToken 第三方渠道标识
     *@param i platform  第三方渠道类型
     */
    @Override
    protected void handleActionThirdId(String s, int i) {
        // 示例代码
        Intent intent1 = new Intent("tt-action");
        String extra = "third token:=" + s + "channel" + i;
        intent1.putExtra("intent :", extra);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
        Log.e("push-test", "onHandleIntent sendLocalBroadcast : " + intent1.toString());
        thirdToken = s;
        platform = i;
    }
}
