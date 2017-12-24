package com.example.zhong.keeps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button addUser = findViewById(R.id.reg);
        Button back = findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar2);
        final EditText account_reg = findViewById(R.id.account_reg);
        final EditText password_reg = findViewById(R.id.password_reg);

        setSupportActionBar(toolbar);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insert User
                UserInfo user = new UserInfo();
                user.setUser(account_reg.getText().toString());
                user.setPassword(password_reg.getText().toString());
                user.save();
            }
        });

        //Back Main
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,
                        MainActivity.class);
                startActivity(intent);

            }
        });

    }

    public void onUserRegisterReturn(final Boolean ok) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ok) {
                    // if login succeeded
                } else {
                    // if failed
                }
            }
        });
    }
}
