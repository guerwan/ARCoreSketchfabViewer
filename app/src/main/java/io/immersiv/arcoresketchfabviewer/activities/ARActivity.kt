package io.immersiv.arcoresketchfabviewer.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import io.immersiv.arcoresketchfabviewer.R
import io.immersiv.arcoresketchfabviewer.SketchfabService
import io.immersiv.arcoresketchfabviewer.models.DownloadResultModel
import io.immersiv.arcoresketchfabviewer.models.SearchResultModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ARActivity : AppCompatActivity() {

    private var arFragment: ArFragment? = null
    private var renderable: ModelRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

        val fileName = intent.getStringExtra(EXTRA_FILE_NAME)
        val path = this@ARActivity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).absolutePath + "/" + fileName

        ModelRenderable.builder()
            .setSource(
                this, RenderableSource.builder().setSource(
                    this,
                    Uri.parse(path),
                    RenderableSource.SourceType.GLTF2
                )
                    .setScale(0.5f)  // Scale the original model to 50%.
                    .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                    .build()
            )
            .setRegistryId(fileName)
            .build()
            .thenAccept { renderable -> this.renderable = renderable }
            .exceptionally { throwable ->
                throwable.printStackTrace()
                val toast = Toast.makeText(this, "Unable to load renderable $fileName", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }


        arFragment?.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (renderable == null) {
                return@setOnTapArPlaneListener
            }

            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment?.arSceneView?.scene)

            // Create the transformable andy and add it to the anchor.
            val andy = TransformableNode(arFragment?.transformationSystem)
            andy.setParent(anchorNode)
            andy.renderable = renderable
            andy.scaleController.minScale = 0.125f
            andy.scaleController.maxScale = 2f
            andy.select()
        }

        //SketchfabService.search(this, "android", SearchCallback())
    }

    inner class SearchCallback : Callback<SearchResultModel> {
        override fun onFailure(call: Call<SearchResultModel>, t: Throwable) {
            Toast.makeText(this@ARActivity, "failure", Toast.LENGTH_SHORT).show()
        }

        override fun onResponse(call: Call<SearchResultModel>, response: Response<SearchResultModel>) {
            val results = response.body()?.results
            if (results?.isNotEmpty() == true) {
                SketchfabService.download(
                    this@ARActivity,
                    results[0].uid,
                    DownloadCallback()
                )
            }
        }
    }

    inner class DownloadCallback : Callback<DownloadResultModel> {
        override fun onFailure(call: Call<DownloadResultModel>, t: Throwable) {
            Toast.makeText(this@ARActivity, "failure", Toast.LENGTH_SHORT).show()
        }

        override fun onResponse(call: Call<DownloadResultModel>, response: Response<DownloadResultModel>) {
            Toast.makeText(this@ARActivity, "success", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_FILE_NAME = "NAME"
        fun newIntent(context: Context, fileName: String): Intent {
            val intent = Intent(context, ARActivity::class.java)
            intent.putExtra(EXTRA_FILE_NAME, fileName)
            return intent
        }
    }
}
