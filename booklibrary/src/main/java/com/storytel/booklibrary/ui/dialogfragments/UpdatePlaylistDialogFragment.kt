package com.storytel.booklibrary.ui.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.storytel.booklibrary.databinding.UpdatePlaylistDialogFragmentBinding
import com.storytel.booklibrary.ui.BookLibraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class UpdatePlaylistDialogFragment: DialogFragment() {

    companion object {
        fun newInstance(playlistId: Long) = UpdatePlaylistDialogFragment().apply { arguments = Bundle()
                .apply { putLong(EXTRA_PLAYLISTID, playlistId) }
        }
    }

    @Inject
    lateinit var viewModel: BookLibraryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return UpdatePlaylistDialogFragmentBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getLong(EXTRA_PLAYLISTID, -1L)?: -1
        val binding = UpdatePlaylistDialogFragmentBinding.bind(view)

        binding.updatePlaylistButton.setOnClickListener {
            viewModel.updatePlaylist(playlistId, binding.updatePlaylistEditText.text.toString())
            dismiss()
        }
    }
}

private const val EXTRA_PLAYLISTID = "EXTRA_PLAYLISTID"