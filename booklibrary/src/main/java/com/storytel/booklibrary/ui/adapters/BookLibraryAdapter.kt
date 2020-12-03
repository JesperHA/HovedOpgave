package com.storytel.booklibrary.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.storytel.booklibrary.R
import com.storytel.booklibrary.entities.SlBookRelations

class BookLibraryAdapter(private val moreButtonClicklistener: onClickListener, private val bookClicklistener: onClickListener): RecyclerView.Adapter<BookLibraryAdapter.ViewHolder>() {

    var data =  listOf<SlBookRelations>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources
        holder.bookCover.load("https://storytel.com${item.book.largeCover}")
        holder.bookName.text = item.book.name
        holder.bookAuthor.text = item.book.authorAsString
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.book_list_item, parent, false)
        return ViewHolder(view, moreButtonClicklistener, bookClicklistener)
    }

    class ViewHolder(itemView: View, clickListener: onClickListener, bookClicklistener : onClickListener) : RecyclerView.ViewHolder(itemView){

        init {

            itemView.findViewById<ImageButton>(R.id.bookshelf_item_more_button).apply{
                setOnClickListener { clickListener.onClick(adapterPosition) }
            }

            itemView.findViewById<ConstraintLayout>(R.id.item).apply{
                setOnClickListener { bookClicklistener.onClick(adapterPosition) }
            }


        }

        val bookCover: ImageView = itemView.findViewById(R.id.book_cover_image)
        val bookName: TextView = itemView.findViewById(R.id.bookshelf_item_title)
        val bookAuthor: TextView = itemView.findViewById(R.id.bookshelf_item_author)
    }

    interface onClickListener {
        fun onClick(adapterPosition: Int)
    }



}




