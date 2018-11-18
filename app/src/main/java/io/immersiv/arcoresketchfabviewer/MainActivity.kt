package io.immersiv.arcoresketchfabviewer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login.setOnClickListener { startActivity(Intent(this@MainActivity, LoginActivity::class.java)) }
        arMode.setOnClickListener { startActivity(Intent(this@MainActivity, SearchActivity::class.java)) }
    }
}
