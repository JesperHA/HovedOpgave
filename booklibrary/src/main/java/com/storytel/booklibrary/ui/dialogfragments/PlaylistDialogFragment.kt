package com.storytel.booklibrary.ui.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.storytel.booklibrary.databinding.PlaylistDialogFragmentBinding
import com.storytel.booklibrary.ui.BookLibraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistDialogFragment: DialogFragment() {

    companion object {
        fun newInstance(playlistId: Long) = PlaylistDialogFragment().apply { arguments = Bundle()
                .apply { putLong(EXTRA_PLAYLISTID, playlistId) }
        }
    }

    @Inject
    lateinit var viewModel: BookLibraryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return PlaylistDialogFragmentBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = PlaylistDialogFragmentBinding.bind(view)
        val playlistId = arguments?.getLong(EXTRA_PLAYLISTID, -1L)?: -1

        binding.deletePlaylistTextView.setOnClickListener {
            viewModel.deletePlaylist(playlistId)
            dismiss()
        }

        binding.updatePlaylistNameTextView.setOnClickListener {
            val fragment = UpdatePlaylistDialogFragment.newInstance(playlistId)
            fragment.show(childFragmentManager, "update_playlist_dialog_fragment")
        }
    }
}

private const val EXTRA_PLAYLISTID = "EXTRA_PLAYLISTID"