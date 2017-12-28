package com.example.zhong.keeps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_network_test.*

class NetworkTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_test)
        bt_login.setOnClickListener {
            userLoginTest("root", "123456", NetworkTestActivity@this)
        }
        bt_xml.setOnClickListener {
            //initKnowledgePoints("root","123456", NetworkTestActivity@this)
        }
        bt_content.setOnClickListener {
            initKnowledgePointsTest("root", "123456", NetworkTestActivity@this)
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
                    markdownView.loadMarkdown(root.childKPList?.get(0)?.markdownContent)
                }
            } else {
                Toast.makeText(NetworkTestActivity@this, "get content failed", Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }
}
