package com.storytel.login.feature.login.forgot

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.storytel.login.R
import com.storytel.login.databinding.LoginFragmentForgotPasswordBinding
import com.storytel.login.pojo.LoginInput
import com.storytel.login.pojo.LoginValidation
import grit.storytel.app.LoginListener
import javax.inject.Inject

class ForgotPasswordFragment: Fragment() {
    @Inject lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var binding: LoginFragmentForgotPasswordBinding
    private var listener: LoginListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = LoginFragmentForgotPasswordBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        val containerHeight = arguments?.getInt(HEIGHT)
        if (containerHeight != null) {
            setProgressContainerHeight(containerHeight)
        }
        val enteredEmail = arguments?.getString(ENTERED_EMAIL)
        enteredEmail?.let { binding.editTextEmail.setText(enteredEmail) }
        binding.buttonResetPassword.setOnClickListener() {
            attemptReset()
        }
        binding.editTextEmail.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                attemptReset()
                return@OnEditorActionListener true
            }
            false
        })
        return binding.root
    }

    private fun attemptReset() {
        viewModel.resetPassword(LoginInput(binding.editTextEmail.id, binding.editTextEmail.text.toString()))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.uiModel().observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel?.let { onUiModelChanged(it) }
        })
    }

    private fun onUiModelChanged(uiModel: ForgotUiModel) {
        if (uiModel.hasResetPassword) {
            onPasswordResetDone()
        } else if (!uiModel.errorMessage.isBlank()) {
            Toast.makeText(context, uiModel.errorMessage, Toast.LENGTH_LONG).show()
        } else if (!uiModel.inputErrorValidation.isEmpty()) {
            showInputError(uiModel.inputErrorValidation)
        }
    }

    private fun onPasswordResetDone() {
        Toast.makeText(context, getString(R.string.alert_message_forgot_password_check_email),
                Toast.LENGTH_LONG).show()
        listener?.onDone()
    }

    private fun showInputError(inputErrorValidation: List<LoginValidation>) {
        inputErrorValidation.reversed().forEach { loginValidation ->
            if (!loginValidation.isValid) {
                when (loginValidation.input.resId) {
                    com.storytel.login.R.id.edit_text_email -> showError(binding.editTextEmail,
                            loginValidation)

                }
            }
        }
    }

    private fun showError(inputField: EditText, errorValidation: LoginValidation) {
        inputField.requestFocus()
        inputField.error = errorValidation.message
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

    private fun setProgressContainerHeight(height: Int) {
        val params = binding.loginOptionsContainer.layoutParams
        params.height = height
        binding.loginOptionsContainer.layoutParams = params
        val paramsProgress = binding.progressContainer.layoutParams
        paramsProgress.height = height
        binding.progressContainer.layoutParams = paramsProgress
    }

    companion object {
        const val ENTERED_EMAIL = "ENTERED_EMAIL"
        const val HEIGHT = "HEIGHT"
        @JvmStatic fun newInstance(enteredEmail: String, loginOptionsContainerHeight: Int) =
                ForgotPasswordFragment().apply {
                    arguments = Bundle().apply {
                        putString(ENTERED_EMAIL, enteredEmail)
                        putInt(HEIGHT, loginOptionsContainerHeight)
                    }
                }
    }
}
