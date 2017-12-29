package com.example.zhong.keeps

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val type = intent.getIntExtra("type", 0)

        when (type) {
            1 -> {
                val content = intent.getStringExtra("content")
                editText.setText(content, TextView.BufferType.EDITABLE)
                bt_save.setOnClickListener {
                    val retIntent = Intent()
                    retIntent.putExtra("content", editText.text.toString().trim())
                    setResult(Activity.RESULT_OK, retIntent)
                    finish()
                }
            }
            2 -> {
                bt_save.setOnClickListener {
                    val retIntent = Intent()
                    retIntent.putExtra("node_name", editText.text.toString().trim())
                    setResult(Activity.RESULT_OK, retIntent)
                    finish()
                }
            }
        }
    }
}