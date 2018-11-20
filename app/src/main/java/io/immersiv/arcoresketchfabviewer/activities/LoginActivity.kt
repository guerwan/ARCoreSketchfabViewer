package io.immersiv.arcoresketchfabviewer.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import io.immersiv.arcoresketchfabviewer.PrefsHelper
import io.immersiv.arcoresketchfabviewer.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.loader.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        contentLayout.visibility = View.GONE
        progressLayout.visibility = View.VISIBLE

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setTitle(R.string.sketchfab_login)

        contentLayout.webViewClient = CustomWebViewClient()
        contentLayout.settings.javaScriptEnabled = true
        contentLayout.loadUrl("https://sketchfab.com/oauth2/authorize/?state=123456789&response_type=token&client_id=EWHr59St60q73pfHUz6REOHGVkTsYY1Nm8JDH4XQ")
    }

    inner class CustomWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            contentLayout.visibility = View.VISIBLE
            progressLayout.visibility = View.GONE
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            if (request?.url?.host == "localhost") {
                val split = request.url.fragment.split("&").firstOrNull { it.contains("access_token") }?.split("=")
                if (split != null && split.size > 1) {
                    val token = split[1]
                    PrefsHelper.saveAccessToken(this@LoginActivity, token)
                }
                finish()
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
