package io.immersiv.arcoresketchfabviewer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.ux.ArFragment

class ARActivity : AppCompatActivity() {

    private var arFragment: ArFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment


        arFragment?.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            Toast.makeText(this@ARActivity, "Coucou", Toast.LENGTH_LONG).show()
        }
    }
}
