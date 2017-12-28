package com.example.zhong.keeps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SearchView searchView;
    private ListView searchListview;
    private KnowledgePoint root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar4);
        searchView = findViewById(R.id.searchView);
        searchListview = findViewById(R.id.search_listview);

        setSupportActionBar(toolbar);

        searchListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //点击搜索按钮时触发
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //搜索内容改变时
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    List<KnowledgePoint> selectKps = selectKPs(root,newText);
                    ChildKPsAdapter adapter = new ChildKPsAdapter(SearchActivity.this,
                            R.layout.child_kp_item, selectKps);
                    searchListview.setAdapter(adapter);
                }
                return false;
            }
        });
    }





    private ArrayList<KnowledgePoint> selectKPs(KnowledgePoint root, String key) {
        ArrayList<KnowledgePoint> kps = new ArrayList<>();
        dfs(kps, root, key);
        return kps;
    }

    private void dfs(ArrayList<KnowledgePoint> kps, KnowledgePoint currentKP, String key) {
        if (currentKP.getName().indexOf(key) != -1) {
            kps.add(currentKP);
        }
        List<KnowledgePoint> childKPList = currentKP.getChildKPList();
        if (childKPList != null) {
            for (KnowledgePoint child:childKPList) {
                dfs(kps, child, key);
            }
        }
    }
}
