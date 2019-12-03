package com.bupt.demo.launcher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.mobile.antui.basic.AUTitleBar;
import com.alipay.mobile.antui.status.AUResultView;
import com.alipay.mobile.common.logging.api.LoggerFactory;
import com.alipay.pushsdk.AliPushInterface;
import com.alipay.pushsdk.data.PushOsType;
import com.mpaas.mas.adapter.api.MPLogger;
import com.mpaas.mps.adapter.api.MPPush;
import com.ut.device.UTDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import alipay.yunpushcore.rpc.ResultPbPB;


/**
 * Created by mPaaS on 16/9/28.
 */
public class MainActivity extends Activity {

    private TextView textView2;

    private String responseContent = "未发送消息";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bupt.demo.launcher.R.layout.main);
        Button btn_click = findViewById(R.id.button2);
        Button btn_reset = findViewById(R.id.button3);
        textView2 = findViewById(R.id.textView2);
        MPPush.init(getApplicationContext());

        //上传
        MPLogger.uploadAll();
        String utdid = UTDevice.getUtdid(getApplicationContext());
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("哈哈哈，我被点击了4");
                Toast.makeText(MainActivity.this, "哈哈哈，我被点击了4",
                        Toast.LENGTH_SHORT).show();
                send();
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText(null);
            }
        });
    }

    private void send() {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://123.57.83.122:8918/web/conference/list");
                    connection = (HttpURLConnection) url.openConnection();
                    //设置请求方法
                    connection.setRequestMethod("GET");
                    //设置连接超时时间（毫秒）
                    connection.setConnectTimeout(5000);
                    //设置读取超时时间（毫秒）
                    connection.setReadTimeout(5000);
                    //返回输入流
                    InputStream in = connection.getInputStream();
                    //读取输入流
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    show(result.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {//关闭连接
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void show(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView2.setText(result);
            }
        });
    }
}
