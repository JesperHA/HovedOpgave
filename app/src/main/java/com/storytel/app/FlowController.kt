package com.storytel.app

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.storytel.login.feature.LoggedInNavigator
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class FlowController @Inject constructor(@ActivityContext private val context: Context): LoggedInNavigator {
    override fun onSignedIn() {
        Toast.makeText(context, "signed in", Toast.LENGTH_LONG).show()
        Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }.also { context.startActivity(it) }
    }
}