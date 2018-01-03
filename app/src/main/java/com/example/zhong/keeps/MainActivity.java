package com.example.zhong.keeps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int SETTINGS = 1;
    private final int SEARCH = 2;
    private final int ADD_NODE = 3;
    private ListView listView;
    private TextView root_textView;
    private Toolbar toolbar;
    public static String account, password;
    public static KnowledgePoint root, currentKP;
    private ChildKPsAdapter adapter;
    private String node_name;
    public static Boolean offline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        root_textView = findViewById(R.id.root_textView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        offline = getIntent().getBooleanExtra("offline", false);

        Button back = findViewById(R.id.bt_back);
        final Button content = findViewById(R.id.bt_content);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentKP != root){
                    currentKP = currentKP.getParentKP();
                    drawCurrentKP();
                } else {
                    Toast.makeText(MainActivity.this,"已经是根节点"
                            ,Toast.LENGTH_SHORT).show();
                }

            }
        });
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                intent.putExtra("content", currentKP.getMarkdownContent());
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentKP = currentKP.getChildKPList().get(i);
                drawCurrentKP();
            }
        });

        if (!offline) {
            //Query
            List<UserInfo> userInfos = DataSupport.select("user","password")
                    //.where("online = ?","1")
                    .find(UserInfo.class);

            account = userInfos.get(0).getUser();
            password = userInfos.get(0).getPassword();


            UtilKt.initKnowledgePoints(account, password, MainActivity.this);
        } else {
            account = "offline";
            password = "123456";
            UtilKt.addOfflineUser(account, password, MainActivity.this);
            root = new KnowledgePoint("root", "", null,
                    new LinkedList<KnowledgePoint>());
            currentKP = root;
            drawCurrentKP();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataSupport.deleteAll(UserInfo.class);
    }

    private void drawCurrentKP() {
        initCurrentTitle();
        initTitles();
    }

    //current_textView
    private void initCurrentTitle(){
        root_textView.setText(currentKP.getName());
    }

    private void initTitles(){
        List<KnowledgePoint> items = currentKP.getChildKPList();
        adapter = new ChildKPsAdapter(MainActivity.this,
                R.layout.child_kp_item, items);
        Log.d("fuck item", items.size() + "");
        if (items.isEmpty()) {
            listView.setVisibility(View.GONE);
        } else {
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
        }
    }

    public void onInitKnowledgePointsReturn(final Boolean ok, final KnowledgePoint kp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ok) {
                    root = kp;
                    currentKP = kp;
                    drawCurrentKP();
                } else {
                    // if failed
                    Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTINGS: {
                if (resultCode == RESULT_OK) {
                    UtilKt.initKnowledgePoints(account,password,MainActivity.this);
                }
                break;
            }
            case SEARCH: {
                if (resultCode == RESULT_OK) {
                    drawCurrentKP();
                }
                break;
            }
            case ADD_NODE:  {
                if (resultCode == RESULT_OK){
                    node_name = data.getStringExtra("node_name");
                    Log.d("fuck node name", node_name);
                    if (node_name != null){
                        KnowledgePoint newKP = new KnowledgePoint(node_name.trim(), "",
                                currentKP, new LinkedList<KnowledgePoint>());
                        currentKP.getChildKPList().add(newKP);
                        initTitles();
                        UtilKt.saveDataChanges(account,password,MainActivity.this,root);
                    }
                }
                break;
            }
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
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
            case R.id.add_node:
                Intent intent_add = new Intent(MainActivity.this, EditActivity.class);
                intent_add.putExtra("type",2);
                startActivityForResult(intent_add,ADD_NODE);
                break;
            case R.id.delete_node:
                if (currentKP != root){
                    //删除当前节点
                    List<KnowledgePoint> currentKpList = currentKP.getParentKP().getChildKPList();
                    for (KnowledgePoint same_class_Kp:currentKpList){
                        if (same_class_Kp.getName().equals(currentKP.getName())){
                           int index = currentKpList.indexOf(same_class_Kp);
                           currentKP.getParentKP().getChildKPList().remove(index);
                        }
                    }
                    currentKP = currentKP.getParentKP();
                    drawCurrentKP();
                    UtilKt.saveDataChanges(account,password,MainActivity.this,root);
                } else {
                    Toast.makeText(MainActivity.this,"当前为根节点，无法删除",
                            Toast.LENGTH_LONG).show();
                }
                break;

            default:
        }
        return true;
    }
}
