package com.storytel.booklibrary.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.storytel.booklibrary.R
import com.storytel.booklibrary.databinding.SearchFragmentBinding
import com.storytel.booklibrary.ui.BookLibraryViewModel
import com.storytel.booklibrary.ui.adapters.BookLibraryAdapter
import com.storytel.booklibrary.ui.dialogfragments.BookLibraryDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    @Inject
    lateinit var viewModel: BookLibraryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.title = "Search"
        return SearchFragmentBinding.inflate(inflater).root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = SearchFragmentBinding.bind(view)

        val adapter = BookLibraryAdapter(object : BookLibraryAdapter.onClickListener {
            override fun onClick(adapterPosition: Int) {
                val adapt = binding.searchBooks.adapter as BookLibraryAdapter
                val fragment = BookLibraryDialogFragment.newInstance(adapt.data[adapterPosition].slBook.slbookId)
                fragment.show(childFragmentManager, "book_library_dialog_fragment")

            }

        }, object : BookLibraryAdapter.onClickListener {
            override fun onClick(adapterPosition: Int) {
                val adapt = binding.searchBooks.adapter as BookLibraryAdapter
                val fragment = BookDetailsFragment.newInstance(adapt.data[adapterPosition].slBook.slbookId)
                activity?.supportFragmentManager
                        ?.beginTransaction()?.replace(R.id.frame_layout, fragment)
                        ?.commit()
            }
        })

        binding.searchEditText.onChange { viewModel.setSearchField(binding.searchEditText.text.toString()) }

        binding.searchBooks.adapter = adapter

        viewModel.searchFieldShow.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })
    }
}

fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {  }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
    })
}