package com.storytel.booklibrary.entities


import androidx.room.*



@Entity(tableName = "history_table")
data class HistoryEntity (

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "history_order")
        val orderId: Int,

        @ColumnInfo(name = "sl_book_id")
        val slBookId: Long
)