package com.example.zhong.keeps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        val content = intent.getStringExtra("content");
        if (content != null) {
            markdownView.loadMarkdown(content)
        } else {
            markdownView.loadMarkdown("no markdown note yet")
        }
    }
}
