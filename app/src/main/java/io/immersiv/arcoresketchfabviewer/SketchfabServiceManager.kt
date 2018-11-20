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
    private const val AUTH_TOKEN = "Authorization"
    private const val BEARER = "Bearer %s"
    private var sToken: String? = null

    fun provideRetrofit(context: Context): Retrofit {
        if (sRetrofit == null) {
            sRetrofit = Retrofit.Builder()
                .baseUrl("https://api.sketchfab.com/v3/")
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        if(sToken == null) {
            val token = PrefsHelper.getAccessToken(context)
            if(token?.isNotEmpty() == true) {
                sToken = String.format(BEARER, token)
            }
        }
        return sRetrofit as Retrofit
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            var request = chain.request()
            val builder = request.newBuilder()

            if (sToken != null) {
                builder.addHeader(AUTH_TOKEN, sToken)
            }

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
