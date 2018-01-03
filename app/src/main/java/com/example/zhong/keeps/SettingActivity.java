package com.example.zhong.keeps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    //private List<Settings> settingsList = new ArrayList<>();
    private Toolbar toolbar;
    private Button log_out,syn;
    private String account,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        log_out = findViewById(R.id.log_out);
        syn = findViewById(R.id.syn);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(UserInfo.class,null,null);
                UserInfo userInfo = new UserInfo();
                userInfo.setOnline(0);
                userInfo.save();
                Intent intent = new Intent(SettingActivity.this,
                        LoginActivity.class);
                startActivity(intent);
            }
        });

        syn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.offline){
                    account = MainActivity.account;
                    password = MainActivity.password;
                    UtilKt.syncDataToServer(account,password,SettingActivity.this);
                } else {
                    Toast.makeText(SettingActivity.this,"离线状态，无法同步",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("fuck", "fuck here");
    }

    public void onSyncDataToServerReturn(final Boolean ok) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ok) {
                    setResult(1);
                    Toast.makeText(SettingActivity.this,"同步成功"
                    ,Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("fuck", "final fuck");
                    Toast.makeText(SettingActivity.this,"同步失败"
                    ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
