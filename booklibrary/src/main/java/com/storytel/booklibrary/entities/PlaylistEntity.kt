package com.storytel.booklibrary.entities


import androidx.room.*



@Entity(tableName = "playlist_table")
data class PlaylistEntity (

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "playlist_id")
        val playlistId: Long,

        @ColumnInfo(name = "playlist_name")
        val playlistName: String


)
