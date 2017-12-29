package com.example.zhong.keeps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private List<KnowledgePoint> items;
    private String node_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        root_textView = findViewById(R.id.root_textView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //Query
        List<UserInfo> userInfos = DataSupport.select("user","password")
                //.where("online = ?","1")
                .find(UserInfo.class);

        account = userInfos.get(0).getUser();
        password = userInfos.get(0).getPassword();

        UtilKt.initKnowledgePoints(account, password, MainActivity.this);
        Button back = findViewById(R.id.bt_back);
        Button content = findViewById(R.id.bt_content);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentKP = currentKP.getParentKP();
                drawCurrentKP();
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
        if (currentKP.getChildKPList() != null) {
            items = currentKP.getChildKPList();
            adapter.notifyDataSetChanged();
        } else {
            items.clear();
            adapter.notifyDataSetChanged();
        }
        listView.setAdapter(adapter);
    }

    public void onInitKnowledgePointsReturn(final Boolean ok, final KnowledgePoint kp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ok) {
                    root = kp;
                    currentKP = kp;
                    items = currentKP.getChildKPList();
                    adapter = new ChildKPsAdapter(MainActivity.this,
                            R.layout.child_kp_item, items);
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
            case ADD_NODE:  {
                if (resultCode == 1){
                    node_name = data.getStringExtra("node_name");
                    if (node_name.trim() != null){
                        KnowledgePoint newKP = null;
                        newKP.setName(node_name);
                        newKP.setParentKP(currentKP.getParentKP());
                        currentKP.getChildKPList().add(newKP);
                        adapter.notifyDataSetChanged();
                        UtilKt.saveDataChanges(account,password,MainActivity.this,root);
                    }
                }
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
