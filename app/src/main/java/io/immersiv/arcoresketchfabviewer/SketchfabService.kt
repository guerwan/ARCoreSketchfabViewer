package io.immersiv.arcoresketchfabviewer

import android.content.Context
import io.immersiv.arcoresketchfabviewer.models.DownloadResultModel
import io.immersiv.arcoresketchfabviewer.models.SearchResultModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


object SketchfabService {

    private interface API {
        @GET("search")
        fun search(
            @Query("type") models: String,
            @Query("downloadable") downloadable: Boolean,
            @Query("staffpicked") staffPicked: Boolean,
            @Query("q") query: String?
        ): Call<SearchResultModel>

        @GET("models/{UID}/download")
        fun download(@Path("UID") uid: String): Call<DownloadResultModel>
    }

    fun search(
        context: Context,
        query: String?,
        staffPicked: Boolean = false,
        callback: Callback<SearchResultModel>
    ): Call<SearchResultModel> {

        val retrofit = SketchfabServiceManager.provideRetrofit(context)

        val github = retrofit.create(API::class.java)

        val call = github.search("models", true, staffPicked, query)

        call.enqueue(callback)

        return call
    }

    fun download(
        context: Context,
        uid: String,
        callback: Callback<DownloadResultModel>
    ): Call<DownloadResultModel> {

        val retrofit = SketchfabServiceManager.provideRetrofit(context)

        val github = retrofit.create(API::class.java)

        val call = github.download(uid)

        call.enqueue(callback)

        return call
    }

}
