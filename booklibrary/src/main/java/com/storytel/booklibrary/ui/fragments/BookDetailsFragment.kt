package com.storytel.booklibrary.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.load
import com.storytel.booklibrary.data.BookInfoWrapper
import com.storytel.booklibrary.databinding.BookDetailsFragmentBinding
import com.storytel.booklibrary.ui.BookLibraryViewModel
import com.storytel.booklibrary.ui.dialogfragments.BookLibraryDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class BookDetailsFragment: Fragment() {

    companion object {
        fun newInstance(slBookID: Long) = BookDetailsFragment().apply { arguments = Bundle().apply { putLong(EXTRA_SLBOOKID, slBookID) } }
    }

    @Inject
    lateinit var viewModel: BookLibraryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.title = "My Bookshelf"
        return BookDetailsFragmentBinding.inflate(inflater).root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val slBookId = arguments?.getLong(EXTRA_SLBOOKID, -1L)?: -1
        viewModel.fetchBookDetails(slBookId)
        val binding = BookDetailsFragmentBinding.bind(view)

        binding.floatingActionButton.setOnClickListener {
            val fragment = BookLibraryDialogFragment.newInstance(slBookId)
            fragment.show(childFragmentManager, "book_library_dialog_fragment")
        }

        val desc = binding.bookDescription
        desc.setOnClickListener{
            Timber.i("col: " + isCollapsed)
            if(isCollapsed){
                desc.maxLines = Integer.MAX_VALUE
                binding.clickTextButton.text = "Tryk på teksten for at vise mindre"

            } else {
                desc.maxLines = MAX_LINES_COLLAPSED
                binding.clickTextButton.text = "Tryk på teksten for at læse mere"

            }
            isCollapsed = !isCollapsed
        }

        viewModel.bookDetails.observe(viewLifecycleOwner, Observer {
            insertBookDetails(binding, it)

        })


    }

    private fun insertBookDetails(binding: BookDetailsFragmentBinding, it: BookInfoWrapper) {
        binding.bookCoverImage.load("https://storytel.com" + it.book.largeCover)
        binding.bookGrade.text = "Rating: " + it.book.grade.toString() + " - Anmendelser: " + it.book.nrGrade.toString()
        binding.bookTitle.text = it.book.name
        if (it.book.authorsAsString.isNullOrEmpty()) {
            binding.bookBy.visibility = View.GONE
        } else {
            binding.bookBy.text = "Af: " + it.book.authorsAsString
        }

        if (it.aBook?.narratorAsString.isNullOrEmpty()) {
            binding.bookWith.visibility = View.GONE
        } else {
            binding.bookWith.text = "Med: " + it.aBook?.narratorAsString
        }

        if (it.aBook?.lengthInHHMM.isNullOrEmpty()) {
            binding.bookLength.visibility = View.GONE
        } else {
            binding.bookLength.text = it.aBook?.lengthInHHMM
        }

        if (it.aBook?.description.isNullOrEmpty()) {
            binding.bookDescription.text = it.eBook?.description
        } else {
            binding.bookDescription.text = it.aBook?.description
        }

        binding.bookDescription.maxLines = MAX_LINES_COLLAPSED
        binding.bookDescription.ellipsize = TextUtils.TruncateAt.END

        if (it.aBook?.publisher?.name.isNullOrEmpty()){
            binding.publisherAudio.visibility = View.GONE
        } else {
            binding.publisherAudio.text = "© " + it.aBook?.publisher?.name + " Lydbog"
        }

        if (it.eBook?.publisher?.name.isNullOrEmpty()){
            binding.publisherEbook.visibility = View.GONE
        } else {
            binding.publisherEbook.text = "© " + it.eBook?.publisher?.name + " E-bog"
        }

    }

}

private val MAX_LINES_COLLAPSED = 7
private val INITIAL_IS_COLLAPSED = true
private var isCollapsed = INITIAL_IS_COLLAPSED

private const val EXTRA_SLBOOKID = "EXTRA_SLBOOKID"