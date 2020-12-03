package com.storytel.login.feature.welcome

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.storytel.login.databinding.LoginFragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import grit.storytel.app.LoginListener
import javax.inject.Inject

@AndroidEntryPoint class WelcomeFragment: Fragment() {
    private lateinit var binding: LoginFragmentWelcomeBinding
    private var listener: LoginListener? = null
    @Inject lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding =
                com.storytel.login.databinding.LoginFragmentWelcomeBinding.inflate(inflater, container, false)
        binding.welcomeVideo.hideController()
        binding.welcomeVideo.useController = false
        binding.welcomeVideo.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        binding.login.setOnClickListener {
            listener?.onLoginSelected(it)
        }
        binding.createAccount.setOnClickListener { listener?.onCreateAccountSelected() }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let {
            lifecycle.addObserver(VideoPlayerComponent(it, binding.welcomeVideo, viewModel.videoUri))
            lifecycle.addObserver(TextDescriptionSwitcher(it, binding.textSwitcher, viewModel, Handler(),
                    binding.indicatorContainer))
        }
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

    companion object {
        @JvmStatic fun newInstance() = WelcomeFragment()
    }
}

