package io.immersiv.arcoresketchfabviewer

import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import io.immersiv.arcoresketchfabviewer.models.DownloadResultModel
import io.immersiv.arcoresketchfabviewer.models.SearchResultModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ARActivity : AppCompatActivity() {

    private var arFragment: ArFragment? = null
    private var duckRenderable: ModelRenderable? = null
    //TODO remove
    private val GLTF_ASSET = "https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/Duck/glTF/Duck.gltf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment


        ModelRenderable.builder()
            .setSource(
                this, RenderableSource.builder().setSource(
                    this,
                    Uri.parse(GLTF_ASSET),
                    RenderableSource.SourceType.GLTF2
                )
                    .setScale(0.5f)  // Scale the original model to 50%.
                    .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                    .build()
            )
            .setRegistryId(GLTF_ASSET)
            .build()
            .thenAccept { renderable -> duckRenderable = renderable }
            .exceptionally { throwable ->
                val toast = Toast.makeText(this, "Unable to load renderable $GLTF_ASSET", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }


        arFragment?.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (duckRenderable == null) {
                return@setOnTapArPlaneListener
            }

            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment?.getArSceneView()?.scene)

            // Create the transformable andy and add it to the anchor.
            val andy = TransformableNode(arFragment?.transformationSystem)
            andy.setParent(anchorNode)
            andy.renderable = duckRenderable
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
            if(results?.isNotEmpty() == true) {
                SketchfabService.download(this@ARActivity, results[0].uid, DownloadCallback())
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
}
