package com.storytel.login.feature.login

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

/**
 * https://developers.facebook.com/docs/app-events/getting-started-app-events-android/#disable-auto-events
 */
class FacebookHandler(private val fragment: Fragment,
                      private val facebookCallback: FacebookCallback<LoginResult>): LifecycleObserver {

    private val permissions = arrayOf("public_profile", "email").asList()
    private val callbackManager = CallbackManager.Factory.create()

    fun beginConnectToFacebook() {
        if (isLoggedIn()) {
            facebookCallback.onSuccess(LoginResult(AccessToken.getCurrentAccessToken(), null, null))
        } else {
            LoginManager.getInstance().registerCallback(callbackManager, facebookCallback)
            LoginManager.getInstance().logInWithReadPermissions(fragment, permissions)
        }
        FacebookSdk.setAutoLogAppEventsEnabled(false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE) fun onStart() {
        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) fun onStop() {
        LoginManager.getInstance().unregisterCallback(callbackManager)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

}