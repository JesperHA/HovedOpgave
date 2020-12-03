package com.storytel.booklibrary.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "a_book_table")
data class ABookEntity (

        @PrimaryKey
        @ColumnInfo(name = "a_book_id")
        var aBookId: Long,
        @ColumnInfo(name = "is_coming_a_book")
        var isComing: Int,
        @ColumnInfo(name = "narrator_as_string")
        var narratorAsString: String,
        @ColumnInfo(name = "release_date_a_book")
        var releaseDateFormat: String


)