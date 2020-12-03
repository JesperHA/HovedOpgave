package com.storytel.booklibrary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.storytel.booklibrary.R
import com.storytel.booklibrary.databinding.PlaylistFragmentBinding
import com.storytel.booklibrary.ui.BookLibraryViewModel
import com.storytel.booklibrary.ui.adapters.PlaylistAdapter
import com.storytel.booklibrary.ui.dialogfragments.CreatePlaylistDialogFragment
import com.storytel.booklibrary.ui.dialogfragments.PlaylistDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    @Inject lateinit var viewModel: BookLibraryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.title = "Your playlists"
        return PlaylistFragmentBinding.inflate(inflater).root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = PlaylistFragmentBinding.bind(view)

        binding.createPlaylistButton.setOnClickListener { onClickCreatePlaylist() }


        val adapter = PlaylistAdapter(object : PlaylistAdapter.OnClickListener {
            override fun onClick(adapterPosition: Int) {
                val adapt = binding.playlistList.adapter as PlaylistAdapter

                val playlistId = adapt.data[adapterPosition].playlistId
                val playlistName = adapt.data[adapterPosition].playlistName

                val playlistBooksFragment = PlaylistBooksFragment.newInstance(playlistId, playlistName)
                activity?.supportFragmentManager
                        ?.beginTransaction()?.replace(R.id.frame_layout, playlistBooksFragment)
                        ?.commit()

            }

        }, object : PlaylistAdapter.OnClickListener {
            override fun onClick(adapterPosition: Int) {
                val adapt = binding.playlistList.adapter as PlaylistAdapter
                val playlistId = adapt.data[adapterPosition].playlistId
                val fragment = PlaylistDialogFragment.newInstance(playlistId)
                fragment.show(childFragmentManager, "playlist_dialog_fragment")

            }

        })

        binding.playlistList.adapter = adapter

        viewModel.uiPlaylists.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })

    }

    private fun onClickCreatePlaylist() {

        val fragment = CreatePlaylistDialogFragment.newInstance(-1)
        fragment.show(childFragmentManager, "create_playlist_dialog_fragment")
    }
}