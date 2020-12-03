package com.storytel.login.feature.welcome

import android.content.Context
import android.os.Handler
import android.text.format.DateUtils
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextSwitcher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.storytel.login.R

class TextDescriptionSwitcher(context: Context, private val textSwitcher: TextSwitcher,
                              private val viewModel: WelcomeViewModel, private val handler: Handler,
                              private val indicatorContainer: LinearLayout): LifecycleObserver {

    private val delay: Long = DateUtils.SECOND_IN_MILLIS * 8L

    private val runnable: Runnable = Runnable {
        setText()
    }

    init {
        val size = context.resources.getDimensionPixelSize(R.dimen.login_welcome_indicator_size)
        val margin = context.resources.getDimensionPixelSize(R.dimen.login_welcome_indicator_margin)
        indicatorContainer.removeAllViews()
        for (i in viewModel.descriptions) {
            val indicator = View(context)
            indicator.setBackgroundResource(R.drawable.indicator_unselected)
            val p = LinearLayout.LayoutParams(size, size)
            p.marginStart = margin
            p.marginEnd = margin
            p.gravity = Gravity.CENTER_VERTICAL
            indicator.layoutParams = p
            indicatorContainer.addView(indicator)
        }

        val inAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        val out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        textSwitcher.inAnimation = inAnim
        textSwitcher.outAnimation = out
        setText()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START) fun onStart() {
        handler.postDelayed(runnable, delay)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP) fun onStop() {
        handler.removeCallbacks(runnable)
    }

    private fun setText() {
        textSwitcher.setText(viewModel.getNextText())
        setIndicatorBg()
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, delay)
    }

    private fun setIndicatorBg() {
        val currentIndex = viewModel.getCurrentIndex()
        for (i in 0..(indicatorContainer.childCount-1)) {
            if (i == currentIndex) {
                indicatorContainer.getChildAt(i).setBackgroundResource(R.drawable.indicator_selected)
            } else {
                indicatorContainer.getChildAt(i).setBackgroundResource(R.drawable.indicator_unselected)
            }

        }
    }

}