package com.storytel.booklibrary.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "book_table")
data class BookEntity (

        @PrimaryKey
        @ColumnInfo(name = "book_id")
        val bookId: Long,
        @ColumnInfo(name = "author_as_string_book")
        val authorAsString: String,
        @ColumnInfo(name= "have_read_book")
        val haveRead: Int,
        @ColumnInfo(name = "large_cover")
        val largeCover: String,
        @ColumnInfo(name = "mapping_status")
        val mappingStatus: Int,
        @ColumnInfo(name = "name")
        val name: String,
        @ColumnInfo(name = "release_date_book")
        val releaseDateString: String,
        @ColumnInfo(name = "series_order")
        val seriesOrder: Int
)