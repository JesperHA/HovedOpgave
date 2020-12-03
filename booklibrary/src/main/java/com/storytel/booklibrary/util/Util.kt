package com.storytel.booklibrary.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.storytel.booklibrary.R

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val bookCover: ImageView = itemView.findViewById(R.id.book_cover_image)
    val bookName: TextView = itemView.findViewById(R.id.bookshelf_item_title)

}
