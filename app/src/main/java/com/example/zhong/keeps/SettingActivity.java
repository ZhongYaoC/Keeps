package com.example.zhong.keeps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
                account = MainActivity.account;
                password = MainActivity.password;
                UtilKt.syncDataToServer(account,password,SettingActivity.this);

            }
        });
    }

    public void syncDataToServerReturn(final Boolean ok) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ok) {
                    setResult(1);
                    finish();
                } else {
                    Toast.makeText(SettingActivity.this,"同步失败"
                    ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
