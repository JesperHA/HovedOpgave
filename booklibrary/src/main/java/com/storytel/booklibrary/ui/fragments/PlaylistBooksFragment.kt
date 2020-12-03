package com.storytel.booklibrary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.storytel.booklibrary.R
import com.storytel.booklibrary.databinding.PlaylistBooksFragmentBinding
import com.storytel.booklibrary.ui.BookLibraryViewModel
import com.storytel.booklibrary.ui.adapters.BookLibraryAdapter
import com.storytel.booklibrary.ui.adapters.BookLibraryAdapter.onClickListener
import com.storytel.booklibrary.ui.dialogfragments.RemoveBookDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint class PlaylistBooksFragment: Fragment() {

    companion object {
        fun newInstance(playlistId: Long, playlistName: String) = PlaylistBooksFragment().apply { arguments = Bundle()
                .apply { putLong(EXTRA_PLAYLISTID, playlistId) }
                .apply { putString(EXTRA_PLAYLISTNAME, playlistName) } }
    }

    @Inject lateinit var viewModel: BookLibraryViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return PlaylistBooksFragmentBinding.inflate(inflater).root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val playlistId = arguments?.getLong(EXTRA_PLAYLISTID, -1L)?: -1
        val playlistName = arguments?.getString(EXTRA_PLAYLISTNAME, "default")?: "Default"
        viewModel.loadPlaylist(playlistId)
        viewModel.printOrder(playlistId)

        val binding = PlaylistBooksFragmentBinding.bind(view)
        binding.playlistBooksName.text = playlistName
        val adapter = BookLibraryAdapter(object : onClickListener {
            override fun onClick(adapterPosition: Int) {
                val adapt = binding.playlistBooks.adapter as BookLibraryAdapter

                val fragment = RemoveBookDialogFragment.newInstance(playlistId, adapt.data[adapterPosition].slBook.slbookId, playlistName)
                fragment.show(childFragmentManager, "remove_book_dialog_fragment")

            }

        }, object : onClickListener {
            override fun onClick(adapterPosition: Int) {
                val adapt = binding.playlistBooks.adapter as BookLibraryAdapter
                val fragment = BookDetailsFragment.newInstance(adapt.data[adapterPosition].slBook.slbookId)
                activity?.supportFragmentManager
                        ?.beginTransaction()?.replace(R.id.frame_layout, fragment)
                        ?.commit()
            }
        })



        binding.playlistBooks.adapter = adapter

        viewModel.playlistBooks.observe(viewLifecycleOwner, Observer {it?.let { adapter.data=it }
        })

        itemTouchHelper.attachToRecyclerView(binding.playlistBooks)



    }

    private val itemTouchHelper by lazy {
        // 1. Note that I am specifying all 4 directions.
        //    Specifying START and END also allows
        //    more organic dragging than just specifying UP and DOWN.
        val simpleItemTouchCallback =
                object : ItemTouchHelper.SimpleCallback(UP or
                        DOWN or
                        START or
                        END, 0) {

                    override fun onMove(recyclerView: RecyclerView,
                                        viewHolder: RecyclerView.ViewHolder,
                                        target: RecyclerView.ViewHolder): Boolean {

                        val adapter = recyclerView.adapter as BookLibraryAdapter
//                        val adapt = binding.playlistBooks.adapter as BookLibraryAdapter
//                        binding.playlistBooks.adapter = adapter

                        val from = viewHolder.adapterPosition
                        val to = target.adapterPosition
                        // 2. Update the backing model. Custom implementation in
                        //    MainRecyclerViewAdapter. You need to implement
                        //    reordering of the backing model inside the method.
                        val playlistId = arguments?.getLong(EXTRA_PLAYLISTID, -1L)?: -1
                        viewModel.moveItem(from, to, playlistId)
                        Timber.i("%s%s", "RecyclerView Order: " + from, to)
                        // 3. Tell adapter to render the model update.
                        adapter.notifyItemMoved(from, to)

                        return true
                    }
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                          direction: Int) {
                        // 4. Code block for horizontal swipe.
                        //    ItemTouchHelper handles horizontal swipe as well, but
                        //    it is not relevant with reordering. Ignoring here.
                    }
                }
        ItemTouchHelper(simpleItemTouchCallback)
    }



//    private fun onMoreButton(slBookId: Long) {
//
//        Toast.makeText(context, "ID: ${slBookId}", Toast.LENGTH_SHORT).show()
//    }

}
private const val EXTRA_PLAYLISTID = "EXTRA_PLAYLISTID"
private const val EXTRA_PLAYLISTNAME = "EXTRA_PLAYLISTNAME"
