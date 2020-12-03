package com.storytel.login.feature.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.storytel.login.BuildConfig
import com.storytel.login.databinding.LoginFragmentLoginBinding
import com.storytel.login.pojo.LoginInput
import com.storytel.login.pojo.LoginValidation
import dagger.hilt.android.AndroidEntryPoint
import grit.storytel.app.LoginListener
import javax.inject.Inject

@AndroidEntryPoint class LoginFragment: Fragment() {
    @Inject lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentLoginBinding
    private lateinit var facebookHandler: FacebookHandler
    private var listener: LoginListener? = null
    private val textWatcher: TextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            binding.editTextPassword.error = null
            binding.editTextEmail.error = null
            viewModel.clearErrors()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //not used
        }
    }
    private val facebookCallback = object: FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            result?.let {
                viewModel.attemptLoginWithFacebook(result.accessToken)
            }
        }

        override fun onCancel() {
            if (BuildConfig.DEBUG) {
                Log.e("LoginFragmet", "onCancel")
            }
        }

        override fun onError(error: FacebookException?) {
            if (BuildConfig.DEBUG && error != null) {
                Log.e("LoginFragmet", "onError", error)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        facebookHandler = FacebookHandler(this, facebookCallback)
        lifecycle.addObserver(facebookHandler)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = LoginFragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        binding.buttonSignIn.setOnClickListener { attemptLogin() }
        binding.editTextPassword.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        binding.editTextPassword.addTextChangedListener(textWatcher)
        binding.editTextEmail.addTextChangedListener(textWatcher)
        binding.buttonSignInFacebook.setOnClickListener {
            setProgressContainerHeight()
            facebookHandler.beginConnectToFacebook()
        }
        binding.buttonForgotPassword.setOnClickListener {
            listener?.onResetPasswordSelected(it, binding.editTextEmail.text.toString(),
                    binding.loginOptionsContainer.height)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.uiModel().observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel?.let { onLoginUiModelChanged(it) }
        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            enterTransition = android.transition.Slide()
            exitTransition = android.transition.Slide()
        }
    }

    private fun onLoginUiModelChanged(uiModel: UserLoginUiModel) {
        if (uiModel.isLoggedIn) {
            onSignedIn()
        } else if (!uiModel.errorMessage.isBlank()) {
            showError(binding.editTextPassword, uiModel.errorMessage)
        } else if (!uiModel.inputErrorValidation.isEmpty()) {
            showInputError(uiModel.inputErrorValidation)
        }
    }

    private fun onSignedIn() {
        listener?.onDone()
        listener?.onSignedIn()
    }

    private fun showInputError(inputErrorValidation: List<LoginValidation>) {
        inputErrorValidation.reversed().forEach { loginValidation ->
            if (!loginValidation.isValid) {
                when (loginValidation.input.resId) {
                    com.storytel.login.R.id.edit_text_email    -> showError(binding.editTextEmail,
                            loginValidation.message)
                    com.storytel.login.R.id.edit_text_password -> showError(binding.editTextPassword,
                            loginValidation.message)

                }
            }
        }
    }

    private fun showError(inputField: EditText, message: String) {
        inputField.requestFocus()
        inputField.error = message
    }

    private fun attemptLogin() {
        setProgressContainerHeight()
        showKeyboard(context, binding.editTextPassword, false)
        val uId = LoginInput(binding.editTextEmail.id, binding.editTextEmail.text.toString())
        val password = LoginInput(binding.editTextPassword.id, binding.editTextPassword.text.toString())
        viewModel.attemptLogin(uId, password)
    }

    private fun showKeyboard(context: Context?, view: View, show: Boolean) {
        if (context != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (show) {
                imm.showSoftInput(view, 0)
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            } else {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookHandler.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun setProgressContainerHeight() {
        val height = binding.loginOptionsContainer.height
        val params = binding.progressContainer.layoutParams
        params.height = height
        binding.progressContainer.layoutParams = params
    }

    companion object {
        @JvmStatic fun newInstance() = LoginFragment()
    }
}
