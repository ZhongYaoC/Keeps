package com.example.zhong.keeps

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_test.*
import java.io.File

class TestActivity : AppCompatActivity() {
    lateinit var root: KnowledgePoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val userDataDir = File(filesDir, "userdata")
        userDataDir.mkdir()
        val userDir = File(userDataDir, "root")
        userDir.mkdir()
        val userContentDir = File(userDir, "content")
        userContentDir.mkdir()

        bt_login.setOnClickListener {
            userLoginTest("root", "123456", NetworkTestActivity@this)
        }
        bt_content.setOnClickListener {
            initKnowledgePointsTest("root", "123456", NetworkTestActivity@this)
        }
        bt_save.setOnClickListener {
            saveDataChanges("root", "123456", TestActivity@this, root)
        }
        bt_upload.setOnClickListener {
            syncDataToServerTest("root", "123456", TestActivity@this)
        }
        bt_save_ip.setOnClickListener {
            ip = et_ip.text.toString().trim()
            Toast.makeText(TestActivity@this, "current server ip $ip", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                markdownView.loadMarkdown(data?.getStringExtra("content"))
            }
        }
    }

    fun onLoginResponse(ok: Boolean) {
        runOnUiThread(Runnable {
            if (ok) {
                Toast.makeText(NetworkTestActivity@this, "login success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(NetworkTestActivity@this, "login failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onInitKnowledgePointsReturn(ok: Boolean, root: KnowledgePoint?) {
        runOnUiThread {
            if (ok) {
                if (root != null && root.childKPList != null) {
                    this.root = root
                    markdownView.loadMarkdown(root.childKPList?.get(0)?.markdownContent)
                }
            } else {
                Toast.makeText(NetworkTestActivity@this, "get content failed", Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    fun onSyncDataToServerReturn(ok: Boolean) {
        runOnUiThread {
            Toast.makeText(TestActivity@this, "is sync working $ok", Toast.LENGTH_LONG).show()
        }
    }
}
