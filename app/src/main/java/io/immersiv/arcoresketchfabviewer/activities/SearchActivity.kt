package io.immersiv.arcoresketchfabviewer.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import io.immersiv.arcoresketchfabviewer.ModelAdapter
import io.immersiv.arcoresketchfabviewer.R
import io.immersiv.arcoresketchfabviewer.SketchfabService
import io.immersiv.arcoresketchfabviewer.models.DownloadResultModel
import io.immersiv.arcoresketchfabviewer.models.SearchResultModel
import io.immersiv.arcoresketchfabviewer.models.SketchfabModel
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.loader.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity(), ModelAdapter.OnItemClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        contentLayout.visibility = View.GONE
        progressLayout.visibility = View.GONE

        contentLayout.adapter = ModelAdapter(this)
        contentLayout.layoutManager = LinearLayoutManager(this@SearchActivity)

        searchView.setOnQueryTextListener(OnQueryTextListener())

        refreshData(null)
    }

    inner class OnQueryTextListener : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            refreshData(query)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    private fun refreshData(query: String?) {
        contentLayout.scrollToPosition(0)
        if (query != null) {
            SketchfabService.search(this, query, false, SearchCallback())
            contentLayout.visibility = View.GONE
            progressLayout.visibility = View.VISIBLE
        } else {
            SketchfabService.search(this, null, true, SearchCallback())
            contentLayout.visibility = View.GONE
            progressLayout.visibility = View.VISIBLE
        }
    }

    override fun onItemClicked(position: Int, model: SketchfabModel?) {
        if (model != null) {
            SketchfabService.download(
                this@SearchActivity,
                model.uid,
                DownloadCallback()
            )
            contentLayout.visibility = View.GONE
            progressLayout.visibility = View.VISIBLE
        }
    }

    inner class SearchCallback : Callback<SearchResultModel> {
        override fun onFailure(call: Call<SearchResultModel>, t: Throwable) {
            Toast.makeText(this@SearchActivity, "failure", Toast.LENGTH_SHORT).show()
            (contentLayout.adapter as ModelAdapter).setData(null)
            contentLayout.visibility = View.VISIBLE
            progressLayout.visibility = View.GONE
        }

        override fun onResponse(call: Call<SearchResultModel>, response: Response<SearchResultModel>) {
            val results = response.body()?.results
            (contentLayout.adapter as ModelAdapter).setData(results)
            contentLayout.visibility = View.VISIBLE
            progressLayout.visibility = View.GONE
        }
    }

    inner class DownloadCallback : Callback<DownloadResultModel> {
        override fun onFailure(call: Call<DownloadResultModel>, t: Throwable) {
            Toast.makeText(this@SearchActivity, "failure", Toast.LENGTH_SHORT).show()
            contentLayout.visibility = View.VISIBLE
            progressLayout.visibility = View.GONE
        }

        override fun onResponse(call: Call<DownloadResultModel>, response: Response<DownloadResultModel>) {
            val url = response.body()?.gltf?.url
            if (url != null) {
                startActivity(
                    ARActivity.newIntent(
                        this@SearchActivity,
                        url
                    )
                )
            } else {
                Toast.makeText(this@SearchActivity, "failure", Toast.LENGTH_SHORT).show()
            }
            contentLayout.visibility = View.VISIBLE
            progressLayout.visibility = View.GONE
        }
    }
}
