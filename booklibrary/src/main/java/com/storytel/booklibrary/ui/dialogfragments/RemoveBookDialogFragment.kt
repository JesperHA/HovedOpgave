package com.storytel.booklibrary.ui.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.storytel.booklibrary.R
import com.storytel.booklibrary.databinding.RemoveBookDialogFragmentBinding
import com.storytel.booklibrary.ui.BookLibraryViewModel
import com.storytel.booklibrary.ui.fragments.PlaylistBooksFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RemoveBookDialogFragment: DialogFragment() {

    companion object {
        fun newInstance(playlistId: Long, slBookID: Long, playlistName: String) = RemoveBookDialogFragment().apply { arguments = Bundle()
                .apply { putLong(EXTRA_SLBOOKID, slBookID) }
                .apply { putLong(EXTRA_PLAYLISTID, playlistId) }
                .apply { putString(EXTRA_PLAYLISTNAME, playlistName) }
        }
    }

    @Inject
    lateinit var viewModel: BookLibraryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return RemoveBookDialogFragmentBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = RemoveBookDialogFragmentBinding.bind(view)

        val slBookId = arguments?.getLong(EXTRA_SLBOOKID, -1L)?: -1
        val playlistId = arguments?.getLong(EXTRA_PLAYLISTID, -1L)?: -1
        val playlistName = arguments?.getString(EXTRA_PLAYLISTNAME, "")?: ""

        binding.removeFromPlaylistTextView.setOnClickListener {
            viewModel.removeBookFromPlaylist(playlistId, slBookId)
            val playlistBooksFragment = PlaylistBooksFragment.newInstance(playlistId, playlistName)
            activity?.supportFragmentManager
                    ?.beginTransaction()?.replace(R.id.frame_layout, playlistBooksFragment)
                    ?.commit()
        }
    }
}

private const val EXTRA_PLAYLISTID = "EXTRA_PLAYLISTID"
private const val EXTRA_SLBOOKID = "EXTRA_SLBOOKID"
private const val EXTRA_PLAYLISTNAME = "EXTRA_PLAYLISTNAME"