package com.storytel.login.feature.create.countrypicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.storytel.login.R
import com.storytel.login.pojo.Country
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint class CountryPickerFragment(): Fragment() {
    @Inject lateinit var viewModel: CountryPickerViewModel
    private lateinit var recyclerView: RecyclerView
    private var adapter: CountryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val countries = arguments?.getParcelableArray(EXTRA_COUNTRIES) ?: emptyArray<Country>()
        val selectedCountry = arguments?.getString(EXTRA_SELECTED) ?: ""
        viewModel.setArgumentValues(countries as Array<Country>, selectedCountry)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.login_fragment_country_picker, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.uiModel.observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel?.let { onUiModelChanged(it) }
        })
    }

    private fun onUiModelChanged(uiModel: CountryPickerUiModel) {
        if (adapter == null) {
            adapter = CountryAdapter(LayoutInflater.from(context), uiModel,
                    object: CountryAdapter.OnClickListener {
                        override fun onItemSelected(adapterPosition: Int) {
                            if (adapter != null && adapterPosition != RecyclerView.NO_POSITION) {
                                adapter?.let {
                                    val country = it.getItemAt(adapterPosition)
                                    viewModel.onCountrySelected(country, adapterPosition)
                                }
                            }
                        }
                    })
            recyclerView.adapter = adapter
        } else {
            adapter?.showSelectedCountry(recyclerView, uiModel)
        }
    }

    companion object {
        const val EXTRA_COUNTRIES = "EXTRA_COUNTRIES"
        const val EXTRA_SELECTED = "EXTRA_SELECTED"
        @JvmStatic fun newInstance(countries: Array<Country>, selectedCountry: String) =
                CountryPickerFragment().apply {
                    arguments = Bundle().apply {
                        putParcelableArray(EXTRA_COUNTRIES, countries)
                        putString(EXTRA_SELECTED, selectedCountry)
                    }
                }
    }
}