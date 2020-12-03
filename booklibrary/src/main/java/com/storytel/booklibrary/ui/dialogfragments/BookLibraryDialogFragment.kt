package com.storytel.booklibrary.ui.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.storytel.booklibrary.databinding.BookLibraryDialogFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import com.storytel.booklibrary.ui.BookLibraryViewModel
import com.storytel.booklibrary.ui.adapters.PlaylistAdapter
import javax.inject.Inject

@AndroidEntryPoint
class BookLibraryDialogFragment: DialogFragment() {

    companion object {
        fun newInstance(slBookID: Long) = BookLibraryDialogFragment().apply { arguments = Bundle().apply { putLong(EXTRA_SLBOOKID, slBookID) } }
    }

    @Inject
    lateinit var viewModel: BookLibraryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return BookLibraryDialogFragmentBinding.inflate(inflater).root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        

        val slBookId = arguments?.getLong(EXTRA_SLBOOKID, -1L)?: -1
        val binding = BookLibraryDialogFragmentBinding.bind(view)
        binding.createPlaylist.setOnClickListener {
            onClickCreatePlaylist(slBookId)
        }

        val adapter = PlaylistAdapter(object : PlaylistAdapter.OnClickListener {
            override fun onClick(adapterPosition: Int) {
                val adapt = binding.playlists.adapter as PlaylistAdapter

                val playlistId = adapt.data[adapterPosition].playlistId
                val playlistName = adapt.data[adapterPosition].playlistName

                onClickView(playlistId, slBookId, playlistName)
                dismiss()
            }

        }, object : PlaylistAdapter.OnClickListener {
            override fun onClick(adapterPosition: Int) {}
        })

        binding.playlists.adapter = adapter

        viewModel.uiPlaylists.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })
    }

    private fun onClickView(playlistId: Long, slBookId: Long, playlistName: String) {
        viewModel.insertToPlaylist(playlistId, slBookId)
        Toast.makeText(context, "Added to ${playlistName}", Toast.LENGTH_SHORT).show()
    }

    private fun onClickCreatePlaylist(slBookId: Long) {

        val fragment = CreatePlaylistDialogFragment.newInstance(slBookId)
        fragment.show(childFragmentManager, "create_playlist_dialog_fragment")

    }
}

private const val EXTRA_SLBOOKID = "EXTRA_SLBOOKID"