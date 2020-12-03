package com.storytel.booklibrary.ui.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.storytel.booklibrary.databinding.CreatePlaylistDialogFragmentBinding
import com.storytel.booklibrary.ui.BookLibraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CreatePlaylistDialogFragment: DialogFragment() {

    companion object {
        fun newInstance(slBookId: Long) = CreatePlaylistDialogFragment().apply { arguments = Bundle()
                .apply { putLong(EXTRA_SLBOOKID, slBookId) }
        }
    }

    @Inject
    lateinit var viewModel: BookLibraryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return CreatePlaylistDialogFragmentBinding.inflate(inflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = CreatePlaylistDialogFragmentBinding.bind(view)
        val slBookId = arguments?.getLong(EXTRA_SLBOOKID, -1L)?: -1

        binding.createPlaylistButtonInPlaylist.setOnClickListener {
            viewModel.insertPlaylistAndSlBook(slBookId, binding.createPlaylistEditText.text.toString())
            dismiss()
        }



    }


}

private const val EXTRA_SLBOOKID = "EXTRA_SLBOOKID"