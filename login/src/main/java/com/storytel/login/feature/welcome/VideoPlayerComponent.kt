package com.storytel.login.feature.welcome

import android.content.Context
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.AssetDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.util.Util

class VideoPlayerComponent constructor(private val context: Context, private val playerView: PlayerView,
                                       private val videoUri: Uri): LifecycleObserver {
    private var resumeWindow: Int = 0
    private var resumePosition: Long = 0
    private var player: SimpleExoPlayer? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE) fun onCreate() {
        clearResumePosition()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START) fun onStart() {
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP) fun onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) fun onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        player?.let {
            updateResumePosition()
            it.release()
            player = null
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) fun onResume() {
        if (Util.SDK_INT <= 23) {
            initializePlayer()
        }
    }

    private fun initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(context)
            player?.let { exoPlayer ->
                playerView.player = exoPlayer
                exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;
                exoPlayer.playWhenReady = true
                exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
                exoPlayer.playWhenReady = true
                val haveResumePosition = resumeWindow != C.INDEX_UNSET
                if (haveResumePosition) {
                    exoPlayer.seekTo(resumeWindow, resumePosition)
                }
                val mediaSource = buildMediaSource()
                mediaSource?.let {
                    exoPlayer.prepare(it, !haveResumePosition, false)
                }
            }
        }
    }

    private fun buildMediaSource(): MediaSource? {
        val dataSpec = DataSpec(videoUri)
        val assetDataSource = AssetDataSource(context)
        try {
            assetDataSource.open(dataSpec)
            val factory = DataSource.Factory { assetDataSource }
            return ExtractorMediaSource.Factory(factory).createMediaSource(videoUri)
        } catch (e: AssetDataSource.AssetDataSourceException) {
            e.printStackTrace()
        }
        return null
    }

    private fun updateResumePosition() {
        player?.let { exoPlayer ->
            resumeWindow = exoPlayer.currentWindowIndex
            resumePosition = if (exoPlayer.isCurrentWindowSeekable) Math.max(0, exoPlayer.currentPosition)
            else C.TIME_UNSET
        }

    }

    private fun clearResumePosition() {
        resumeWindow = C.INDEX_UNSET
        resumePosition = C.TIME_UNSET
    }

}