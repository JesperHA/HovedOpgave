package com.storytel.booklibrary.entities

import androidx.room.*

@Entity( tableName = "playlist_sl_book_cross_ref")
data class PlaylistSlBookCrossRef (

        @ColumnInfo(name = "playlist_id")
        val _playlistId: Long = -1,

        @ColumnInfo(name = "sl_book_id")
        val slBookId: Long,

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "playlist_order")
        val order: Int = -1

)

data class PlaylistWithSlBooks(
        @Embedded
        val playlist: PlaylistEntity,

        @Relation(
            parentColumn = "playlist_id",
            entityColumn = "sl_book_id",
                associateBy = Junction(PlaylistSlBookCrossRef::class)
        )
                val playlists: List<SLBookEntity>
)