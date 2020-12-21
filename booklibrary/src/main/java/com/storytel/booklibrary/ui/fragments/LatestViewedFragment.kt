package com.storytel.booklibrary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.storytel.booklibrary.R
import com.storytel.booklibrary.databinding.BookLibraryFragmentBinding
import com.storytel.booklibrary.databinding.LatestViewedFragmentBinding
import com.storytel.booklibrary.ui.BookLibraryViewModel
import com.storytel.booklibrary.ui.adapters.BookLibraryAdapter
import com.storytel.booklibrary.ui.adapters.BookLibraryAdapter.onClickListener
import com.storytel.booklibrary.ui.dialogfragments.BookLibraryDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint class LatestViewedFragment
    : Fragment() {

    companion object {
        fun newInstance() = LatestViewedFragment()
    }

    @Inject lateinit var viewModel: BookLibraryViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.title = "History"
        return LatestViewedFragmentBinding.inflate(inflater).root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.fetchBookshelf()

        val binding = LatestViewedFragmentBinding.bind(view)
        val adapter = BookLibraryAdapter(object : onClickListener {
            override fun onClick(adapterPosition: Int) {
                val adapt = binding.latest.adapter as BookLibraryAdapter
                val fragment = BookLibraryDialogFragment.newInstance(adapt.data[adapterPosition].slBook.slbookId)
                fragment.show(childFragmentManager, "book_library_dialog_fragment")

            }

        }, object : onClickListener {
            override fun onClick(adapterPosition: Int) {
                val adapt = binding.latest.adapter as BookLibraryAdapter
                val fragment = BookDetailsFragment.newInstance(adapt.data[adapterPosition].slBook.slbookId)
                activity?.supportFragmentManager
                        ?.beginTransaction()?.replace(R.id.frame_layout, fragment)
                        ?.commit()
            }
        })

        binding.swipeRefreshLatestViewed.setOnRefreshListener {
            viewModel.fetchBookshelf()
            binding.swipeRefreshLatestViewed.isRefreshing = false
        }

        binding.latest.adapter = adapter

        viewModel.history.observe(viewLifecycleOwner, Observer {it?.let { adapter.data=it }
        })


    }


}