package io.immersiv.arcoresketchfabviewer

import android.content.Context

internal object PrefsHelper {
    private const val SHARED_PREFS_NAME = "PREFS"
    private const val ACCESS_TOKEN = "ACCESS_TOKEN"

    @JvmStatic
    fun saveAccessToken(context: Context, token: String) =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit().putString(ACCESS_TOKEN, token).apply()

    @JvmStatic
    fun getAccessToken(context: Context) =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).getString(ACCESS_TOKEN, null)
}