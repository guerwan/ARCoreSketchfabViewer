package io.immersiv.arcoresketchfabviewer

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object SketchfabServiceManager {
    private const val TAG = "SketchfabServiceManager"
    private var sRetrofit: Retrofit? = null

    fun provideRetrofit(context: Context): Retrofit {
        if (sRetrofit == null) {
            sRetrofit = Retrofit.Builder()
                .baseUrl("https://api.sketchfab.com/v3/")
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return sRetrofit as Retrofit
    }

    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            var request = chain.request()
            val builder = request.newBuilder()

            request = builder.build()

            chain.proceed(request)
        }

        val httpInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d(TAG, message) })
        httpInterceptor.level =
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(httpInterceptor)
            .build()
    }

}
