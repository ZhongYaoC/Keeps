package com.example.zhong.keeps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import us.feras.mdv.MarkdownView;

public class MainActivity extends AppCompatActivity {

    private final int SETTINGS = 1;
    private final int SEARCH = 2;

    //private List<SubTitle> subTitleList = new ArrayList<>();
    private ListView listView;
    private TextView root_textView;
    private MarkdownView markdownView;
    private Toolbar toolbar;
    String account, password;
    public static KnowledgePoint root, currentKP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        root_textView = findViewById(R.id.root_textView);
        markdownView = findViewById(R.id.markdownView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //Query
        List<UserInfo> userInfos = DataSupport.select("user","password")
                .where("online = ?","1")
                .find(UserInfo.class);

        account = userInfos.get(0).toString();
        password = userInfos.get(1).toString();

        UtilKt.initKnowledgePoints(account, password, MainActivity.this);
    }

    private void drawCurrentKP() {
        initCurrentTitle();
        initTitles();
    }

    //current_textView
    private void initCurrentTitle(){
        root_textView.setText(currentKP.getParentKP().toString());
    }

    private void initTitles(){
        ChildKPsAdapter adapter = new ChildKPsAdapter(MainActivity.this,
                R.layout.child_kp_item, currentKP.getChildKPList());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentKP = currentKP.getChildKPList().get(i);
                drawCurrentKP();
            }
        });
    }

    //MarkDown
    private void initContent(){
        markdownView.loadMarkdown(currentKP.getMarkdownContent());
    }

    public void onInitKnowledgePointsReturn(final Boolean ok, final KnowledgePoint kp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ok) {
                    root = kp;
                    currentKP = kp;
                    initCurrentTitle();
                    initTitles();
                    initContent();
                } else {
                    // if failed
                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTINGS: {
                if (resultCode == 1) {
                    UtilKt.initKnowledgePoints(account,password,MainActivity.this);
                }
                break;
            }
            case SEARCH: {
                if (resultCode == 1) {
                    drawCurrentKP();
                }
            }
            default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                //Search
                Intent intent_search = new Intent(MainActivity.this,SearchActivity.class);
                startActivityForResult(intent_search, SEARCH);
                break;
            case R.id.settings:
                //activity
                Intent intent_set = new Intent(MainActivity.this,SettingActivity.class);
                startActivityForResult(intent_set,SETTINGS);
                break;
            default:
        }
        return true;
    }
}
