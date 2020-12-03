package com.storytel.booklibrary.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "e_book_table")
data class EBookEntity (

        @PrimaryKey
        @ColumnInfo(name = "e_book_id")
        val eBookid: Long,
        @ColumnInfo(name = "release_date_e_book")
        val releaseDateFormat: String,
        @ColumnInfo(name = "is_coming_e_book")
        val isComing: Int
)