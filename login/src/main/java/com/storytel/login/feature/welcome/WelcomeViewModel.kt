package com.storytel.login.feature.welcome

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.storytel.login.data.UserCredentialStore
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped class WelcomeViewModel @Inject constructor(val descriptions: Array<String>):
        ViewModel() {

    private var textIndex: Int = -1
    val videoUri = Uri.parse("file:///android_asset/welcome.mp4")

    fun getNextText(): String {
        textIndex++
        return descriptions[getCurrentIndex()]
    }

    fun getCurrentIndex(): Int {
        return textIndex % descriptions.size
    }
}