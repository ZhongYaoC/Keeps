package com.example.zhong.keeps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_content.*

class ContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        val content = MainActivity.currentKP.markdownContent
        if (content != "") {
            markdownView.loadMarkdown(content, "file:///android_asset/foghorn.css")
        } else {
            markdownView.loadMarkdown("no markdown note yet")
        }
    }
}
