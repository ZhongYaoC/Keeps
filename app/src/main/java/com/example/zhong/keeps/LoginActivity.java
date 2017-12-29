package com.example.zhong.keeps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.tablemanager.Connector;

public class LoginActivity extends AppCompatActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login,offline;
    private TextView register;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        login = findViewById(R.id.login);
        offline = findViewById(R.id.offline);
        register = findViewById(R.id.register);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        //Create DB
        Connector.getDatabase();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                UtilKt.userLogin(account, password, LoginActivity.this);
            }
        });

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("offline",true);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        // for test
        Button bt_test = findViewById(R.id.bt_test);
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, TestActivity.class));
            }
        });

    }

    public void onUserLoginReturn(final Boolean ok) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ok) {
                    UserInfo user = new UserInfo();
                    user.setUser(accountEdit.getText().toString());
                    user.setPassword(passwordEdit.getText().toString());
                    user.setOnline(1);
                    user.save();
                    // if login succeeded
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("offline", false);
                    startActivity(intent);
                } else {
                    // if failed
                    Toast.makeText(LoginActivity.this,
                            "account or password is invalid",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}