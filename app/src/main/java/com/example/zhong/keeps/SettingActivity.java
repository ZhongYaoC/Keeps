package com.example.zhong.keeps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    //private List<Settings> settingsList = new ArrayList<>();
    private Toolbar toolbar;
    private Button log_out,syn;

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
                setResult(1);
                finish();
            }
        });

        /*initSetting();
        SettingsAdapter adapter = new SettingsAdapter(SettingActivity.this,
                R.layout.settings,settingsList);
        ListView listView = findViewById(R.id.settings_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 ){//Log out
                    UserInfo userInfo = new UserInfo();
                    userInfo.setOnline(0);
                    userInfo.save();
                    Intent intent = new Intent(SettingActivity.this,
                            LoginActivity.class);

                    startActivity(intent);

                }else if (i == 1){//Syn
                    setResult(1);
                    finish();
                }
            }
        });*/
    }
    /*
    private void initSetting(){
        Settings Logout = new Settings("注销");
        settingsList.add(Logout);
        Settings syn = new Settings("同步");
        settingsList.add(syn);
    }*/
}
