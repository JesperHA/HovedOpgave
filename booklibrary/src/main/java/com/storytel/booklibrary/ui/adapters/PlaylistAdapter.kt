package com.storytel.booklibrary.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.storytel.booklibrary.R
import com.storytel.booklibrary.data.UiPlaylists

class PlaylistAdapter(private val clickListener: OnClickListener, private val clickListener2: OnClickListener) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    var data = listOf<UiPlaylists>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.playlistName.text = item.playlistName
        if (item.urls.size in 1..3) {
            holder.playlistImage.load("https://storytel.com" + item.urls[0])
        } else if (item.urls.size > 3) {
            holder.playlistImage.visibility = View.INVISIBLE

            holder.playlistImageMosaic1.visibility = View.VISIBLE
            holder.playlistImageMosaic1.load("https://storytel.com" + item.urls[0])
            holder.playlistImageMosaic2.visibility = View.VISIBLE
            holder.playlistImageMosaic2.load("https://storytel.com" + item.urls[1])
            holder.playlistImageMosaic3.visibility = View.VISIBLE
            holder.playlistImageMosaic3.load("https://storytel.com" + item.urls[2])
            holder.playlistImageMosaic4.visibility = View.VISIBLE
            holder.playlistImageMosaic4.load("https://storytel.com" + item.urls[3])
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.playlist_item, parent, false)
        return ViewHolder(view, clickListener, clickListener2)
    }


    class ViewHolder(itemView: View, clickListener: OnClickListener, clickListener2: OnClickListener) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener { clickListener.onClick(adapterPosition) }
            itemView.setOnLongClickListener {
                clickListener2.onClick(adapterPosition)
                true
            }

        }

        val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
        val playlistImage: ImageView = itemView.findViewById(R.id.playlist_image)
        val playlistImageMosaic1: ImageView = itemView.findViewById(R.id.playlist_mosaic_image1)
        val playlistImageMosaic2: ImageView = itemView.findViewById(R.id.playlist_mosaic_image2)
        val playlistImageMosaic3: ImageView = itemView.findViewById(R.id.playlist_mosaic_image3)
        val playlistImageMosaic4: ImageView = itemView.findViewById(R.id.playlist_mosaic_image4)
    }

    interface OnClickListener {
        fun onClick(adapterPosition: Int)
    }
}