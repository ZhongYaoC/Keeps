package com.example.zhong.keeps

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        setSupportActionBar(toolbar5)

        val content = intent.getStringExtra("content")
        if (content != null) {
            markdownView.loadMarkdown(content, "file:///android_asset/foghorn.css")
        } else {
            markdownView.loadMarkdown("no markdown note yet")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.update_content,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            R.id.update_content -> {
                val intent = Intent()
                //intent.setClass(ContentActivity@this,)
                //intent.putExtra("content",)
                //startActivity(intent)
            }
        }
        return true
    }
}
