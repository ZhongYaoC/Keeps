package com.example.zhong.keeps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import us.feras.mdv.MarkdownView;

public class MainActivity extends AppCompatActivity {

    private List<SubTitle> subTitleList = new ArrayList<>();
    private ListView listView;
    private TextView root_textView;
    private MarkdownView markdownView;
    private Toolbar toolbar;
    private KnowledgePoint root, currentKP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        root_textView = findViewById(R.id.root_textView);
        markdownView = findViewById(R.id.markdownView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        initRootTitle();    //初始化根目录表标题

        initTitles();   //初始化子文件标题
        SubtitleAdapter adapter = new SubtitleAdapter(MainActivity.this,
                R.layout.sub_title,subTitleList);
        listView.setAdapter(adapter);

        initContent();  //md content

    }

    //root_textView
    private void initRootTitle(){
        //xml ->

    }

    private void initTitles(){
        //xml -> for ()

    }

    //MarkDown
    private void initContent(){
        //markdownView.loadMarkdown("## Hello Markdown");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                //子节点 ?!
                break;
            case R.id.search:
                //差一个Search API
                break;
            case R.id.settings:
                //activity
                break;
            default:
        }
        return true;
    }

    public void onInitKnowledgePointsReturn(final Boolean ok, final KnowledgePoint kp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ok) {
                    List<KnowledgePoint> childList = kp.getChildKPList();

                } else {
                    // if failed
                }
            }
        });
    }
}
