package com.storytel.login.feature.create.countrypicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.storytel.login.R
import com.storytel.login.pojo.Country

class CountryAdapter(private val inflater: LayoutInflater, private var uiModel: CountryPickerUiModel,
                     private val onClickListener: OnClickListener):
        RecyclerView.Adapter<ViewHolderCountry>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCountry {
        return ViewHolderCountry(inflater.inflate(R.layout.login_country_adapteritem, parent, false),
                onClickListener)
    }

    override fun getItemCount(): Int {
        return uiModel.countries.size
    }

    override fun onBindViewHolder(holder: ViewHolderCountry, position: Int) {
        val country = uiModel.countries[position]
        holder.name.text = country.displayName
        holder.image.setImageResource(country.flagRes)
        holder.selected.visibility =
                if (country.iso == null || country.iso != uiModel.selectedCountry) View.GONE else View.VISIBLE
    }

    override fun onBindViewHolder(holder: ViewHolderCountry, position: Int, payloads: MutableList<Any>) {
        if (!payloads.isEmpty() && payloads[0] is Payload) {
            val p = payloads[0] as Payload
            holder.selected.visibility = if (p.isSelected) View.VISIBLE else View.GONE
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    fun getItemAt(adapterPosition: Int): Country {
        return uiModel.countries[adapterPosition]
    }

    fun showSelectedCountry(recyclerView: RecyclerView, ui: CountryPickerUiModel) {
        uiModel = ui
        val positionForCurrentSelectedCountry = findVisibleSelectedCountry(recyclerView)
        if (positionForCurrentSelectedCountry != ui.indexOfSelectedCountry) {
            if (positionForCurrentSelectedCountry != RecyclerView.NO_POSITION) {
                recyclerView.adapter?.notifyItemChanged(positionForCurrentSelectedCountry, Payload(false))
            }
            if (ui.indexOfSelectedCountry != RecyclerView.NO_POSITION) {
                recyclerView.adapter?.notifyItemChanged(ui.indexOfSelectedCountry, Payload(true))
            }
        }
    }

    private fun findVisibleSelectedCountry(recyclerView: RecyclerView): Int {
        val manager = recyclerView.layoutManager as GridLayoutManager
        val first = manager.findFirstVisibleItemPosition()
        val last = manager.findLastVisibleItemPosition()
        for (i in first..last) {
            val vh = recyclerView.findViewHolderForAdapterPosition(i) as ViewHolderCountry
            val selected = vh.selected.visibility == View.VISIBLE

            if (selected) {
                recyclerView.adapter?.notifyItemChanged(i, Payload(false))
            }
        }
        return RecyclerView.NO_POSITION
    }

    interface OnClickListener {
        fun onItemSelected(adapterPosition: Int)
    }

    data class Payload(val isSelected: Boolean)
}

class ViewHolderCountry(v: View, private val onClickListener: CountryAdapter.OnClickListener):
        RecyclerView.ViewHolder(v) {

    val root: View = v.findViewById<View>(R.id.root)
    val selected: View = v.findViewById<View>(R.id.selected)
    val image = v.findViewById<ImageView>(R.id.image_country_flag)
    val name = v.findViewById<TextView>(R.id.textview_country_name)

    init {
        v.setOnClickListener { onClickListener.onItemSelected(adapterPosition) }
    }
}



