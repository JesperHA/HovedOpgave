package com.storytel.login.data

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.storytel.login.pojo.LoginResponse

class SharedPrefsUserCredentialStore(private val context: Context, private val gson: Gson) :
        UserCredentialStore {

    companion object {
        const val PREFS_NAME: String = "user_prefs"
        const val KEY: String = "loginResponse"
    }

    @WorkerThread
    override fun save(loginResponse: LoginResponse) {
        val json = gson.toJson(loginResponse, LoginResponse::class.java)
        getPrefs(context).edit().putString(KEY, json).apply()
    }

    override fun get(): LoginResponse {
        val json = getPrefs(context).getString(KEY, "{}")
        return gson.fromJson(json, LoginResponse::class.java)
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}