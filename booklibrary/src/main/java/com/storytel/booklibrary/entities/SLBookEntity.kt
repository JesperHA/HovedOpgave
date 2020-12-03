package com.storytel.booklibrary.entities

import androidx.room.*


@Entity(tableName = "sl_book_table")
data class SLBookEntity (

        @PrimaryKey
        @ColumnInfo(name = "sl_book_id")
        val slbookId: Long,

        @ColumnInfo(name = "restriction_sl_book")
        val restriction: Int,

        @ColumnInfo(name = "status_sl_book")
        val status: Int,

        @ColumnInfo(name = "book_id")
        val bookId: Long,

        @ColumnInfo(name = "a_book_id")
        val aBookId: Long,

        @ColumnInfo(name = "e_book_id")
        val eBookId: Long


)

data class SlBookRelations(
        @Embedded
        val slBook: SLBookEntity,
        @Relation(
                entity = BookEntity::class,
                parentColumn = "book_id",
                entityColumn = "book_id"
        )
        val book: BookEntity,
        @Relation(
                entity = EBookEntity::class,
                parentColumn = "e_book_id",
                entityColumn = "e_book_id"
        )
        val eBook: EBookEntity,
        @Relation(
                entity = ABookEntity::class,
                parentColumn = "a_book_id",
                entityColumn = "a_book_id"
        )
        val aBook: ABookEntity
)