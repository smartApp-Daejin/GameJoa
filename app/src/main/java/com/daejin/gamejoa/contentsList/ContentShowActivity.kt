package com.daejin.gamejoa.contentsList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.daejin.gamejoa.R

class ContentShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_show)

        val getUrl = intent.getStringExtra("url") // URL 데이터 받아옴

        val webView: WebView = findViewById(R.id.webView)
        webView.loadUrl(getUrl.toString()) // 받오온 getUrl 을 webView 에 load 시켜줌

    }
}