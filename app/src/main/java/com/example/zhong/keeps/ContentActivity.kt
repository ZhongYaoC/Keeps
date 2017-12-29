package com.example.zhong.keeps

import android.app.Activity
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

        val content = MainActivity.currentKP.markdownContent
        if (content != "") {
            markdownView_content.loadMarkdown(content, "file:///android_asset/foghorn.css")
        } else {
            markdownView_content.loadMarkdown("no markdown note yet")
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
                intent.setClass(ContentActivity@this, EditActivity::class.java)
                intent.putExtra("type",1)
                intent.putExtra("content",MainActivity.currentKP.markdownContent)
                startActivityForResult(intent, 1)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val content = data?.getStringExtra("content")
                if (content != null) {
                    markdownView_content.loadMarkdown(content)
                    MainActivity.currentKP.markdownContent = content
                    saveDataChanges(MainActivity.account, MainActivity.password,
                            ContentActivity@this, MainActivity.root)
                }
            }
        }
    }
}
