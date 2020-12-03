package grit.storytel.app

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Fade
import com.storytel.login.R
import com.storytel.login.data.UserAccount
import com.storytel.login.feature.LoggedInNavigator
import com.storytel.login.feature.create.countrypicker.CountryPickerFragment
import com.storytel.login.feature.create.credentials.CreateAccountFragment
import com.storytel.login.feature.login.LoginFragment
import com.storytel.login.feature.login.forgot.ForgotPasswordFragment
import com.storytel.login.feature.welcome.WelcomeFragment
import com.storytel.login.pojo.Country
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint class MainActivity: AppCompatActivity(), LoginListener {
    @Inject lateinit var loggedInNavigator: LoggedInNavigator
    @Inject lateinit var userAccount: UserAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (userAccount.isSignedIn()) {
            loggedInNavigator.onSignedIn()
        } else {
            setContentView(R.layout.login_activity)
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fullscreen_container, WelcomeFragment()).commit()
            }
        }
    }

    override fun onLoginSelected(v: View) {
        val loginFragment = LoginFragment.newInstance()
        loginFragment.enterTransition = androidx.transition.Slide()
        loginFragment.exitTransition = androidx.transition.Slide()
        supportFragmentManager.beginTransaction().replace(R.id.popup_container, loginFragment)
                .addToBackStack("LoginFragment").commit()
    }

    override fun onResetPasswordSelected(v: View, enteredEmail: String, loginOptionsContainerHeight: Int) {
        val current = supportFragmentManager.findFragmentById(R.id.popup_container)
        val fragment = ForgotPasswordFragment.newInstance(enteredEmail, loginOptionsContainerHeight)
        current?.exitTransition = androidx.transition.Fade(Fade.OUT)
        current?.enterTransition = androidx.transition.Fade(Fade.IN)
        fragment.enterTransition = androidx.transition.Fade(Fade.IN)
        fragment.exitTransition = androidx.transition.Fade(Fade.OUT)
        supportFragmentManager.beginTransaction().replace(R.id.popup_container, fragment)
                .addToBackStack("ForgotPasswordFragment").commit()
    }

    override fun onDone() {
        val fragment = supportFragmentManager.findFragmentById(R.id.popup_container)
        if ((fragment is LoginFragment || fragment is ForgotPasswordFragment) && supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onCreateAccountSelected() {
        val fragment = CreateAccountFragment.newInstance()
        fragment.enterTransition = androidx.transition.Slide()
        fragment.exitTransition = androidx.transition.Slide()
        supportFragmentManager.beginTransaction().replace(R.id.popup_container, fragment)
                .addToBackStack("CreateAccountFragment").commit()
    }

    override fun onDisplayCountryPicker(countries: Array<Country>, bestGuessCountry: String) {
        val fragment = CountryPickerFragment.newInstance(countries, bestGuessCountry)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.enterTransition = android.transition.Slide()
            fragment.exitTransition = android.transition.Slide()
        }
        supportFragmentManager.beginTransaction().replace(R.id.popup_container, fragment)
                .addToBackStack("CountryPickerFragment").commit()
    }

    override fun onSignedIn() {
        loggedInNavigator.onSignedIn()
    }
}

interface LoginListener {
    fun onLoginSelected(v: View)
    fun onDone()
    fun onResetPasswordSelected(v: View, enteredEmail: String, loginOptionsContainerHeight: Int)
    fun onCreateAccountSelected()
    fun onDisplayCountryPicker(countries: Array<Country>, bestGuessCountry: String)
    fun onSignedIn()
}
