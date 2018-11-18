package io.immersiv.arcoresketchfabviewer

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
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
        contentLayout.loadUrl("https://sketchfab.com")//TODO
    }

    inner class CustomWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            contentLayout.visibility = View.VISIBLE
            progressLayout.visibility = View.GONE
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
