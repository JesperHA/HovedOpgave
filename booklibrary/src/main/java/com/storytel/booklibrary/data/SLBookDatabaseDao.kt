package com.storytel.booklibrary.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.storytel.booklibrary.entities.*
import timber.log.Timber


@Dao
interface SLBookDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSLBook(slBook: SLBookEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(book: BookEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEBook(eBook: EBookEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertABook(aBook: ABookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistRef(playlistRef: PlaylistSlBookCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToPlaylist(playlistId: Long, slBookId: Long){
        insertPlaylistRef(PlaylistSlBookCrossRef(playlistId, slBookId, 0))
    }

    // might be useless
    @Transaction
    fun insertPlaylistWithCrossRef(slBookId: Long, playlistName: String){
        val playlistId = insertPlaylist(PlaylistEntity(0, playlistName))
        Timber.i("playlistId returned from insertplaylist function: ${playlistId}")
        insertToPlaylist(playlistId, slBookId)

    }

    @Transaction
    fun insertBooksFromApi(entityWrapperList: List<BookEntityWrapper>){
        entityWrapperList.forEach {entityWrapper ->
            insertSLBook(entityWrapper.slBook)
            entityWrapper.aBook?.let { insertABook(it) }
            entityWrapper.eBook?.let { insertEBook(it) }
            insertBook(entityWrapper.book)
        }
    }

    @Update
    fun update(slBook: SLBookEntity)

    @Query("UPDATE playlist_sl_book_cross_ref SET sl_book_id=:slBookId WHERE playlist_order=:playlistOrder")
    suspend fun updatePlaylistCrossRef(slBookId: Long, playlistOrder: Int)

    suspend fun updatePlaylistOrder(from: Int, to: Int, playlistId: Long) {
        val playlistOrderList = getPlaylistOrder(playlistId)

        val fromDB = playlistOrderList[from]
        val toDB = playlistOrderList[to]

        val fromSlBookId = getSpecificBookFromPlaylist(fromDB)
        val toSlBookId = getSpecificBookFromPlaylist(toDB)

        updatePlaylistCrossRef(fromSlBookId, toDB)
        updatePlaylistCrossRef(toSlBookId, fromDB)
    }

    @Query("SELECT playlist_order FROM playlist_sl_book_cross_ref WHERE playlist_id = :key")
    suspend fun getPlaylistOrder(key: Long): List<Int>


    @Query("UPDATE playlist_table SET playlist_name=:playlistName WHERE playlist_id=:key")
    fun updatePlaylist(key: Long, playlistName: String)

    @Query("SELECT * FROM sl_book_table WHERE sl_book_id = :key")
    fun get(key: Long): SlBookRelations

    @Query("SELECT * FROM playlist_table")
    fun getPlaylistWithSLBooks(): LiveData<List<PlaylistWithSlBooks>>

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table")
    fun getPlaylistLiveData(): LiveData<List<PlaylistEntity>>

    @Query("SELECT sl_book_id FROM playlist_sl_book_cross_ref WHERE playlist_id = :key ORDER BY playlist_order ASC")
    suspend fun getPlaylistBookCrossRef(key: Long): List<Long>

    @Query("SELECT sl_book_id FROM playlist_sl_book_cross_ref WHERE playlist_order =:key")
    fun getSpecificBookFromPlaylist(key: Int): Long



    suspend fun printOrder(playlistId: Long){
        val orderList = getPlaylistOrder(playlistId)
        Timber.i("Order: " + orderList)
    }

    @Query("SELECT large_cover FROM sl_book_table INNER JOIN playlist_sl_book_cross_ref ON sl_book_table.sl_book_id = playlist_sl_book_cross_ref.sl_book_id INNER JOIN book_table ON sl_book_table.book_id=book_table.book_id WHERE playlist_id = :playlistId LIMIT 4")
    fun getFourLargeCovers(playlistId: Long): List<String>

    @Query ("SELECT * FROM sl_book_table INNER JOIN book_table ON sl_book_table.book_id = book_table.book_id WHERE name LIKE :input OR author_as_string_book LIKE :input")
    suspend fun getSearchResultsQuery(input: String): List<SlBookRelations>

    @Transaction
    suspend fun getSearchResults(input: String): List<SlBookRelations> {
        val searchString = "%$input%"
        return getSearchResultsQuery(searchString)
    }

    @Transaction
    suspend fun getPlaylistsWithCovers(): List<UiPlaylists>{
        val uiPlaylists = mutableListOf<UiPlaylists>()
        val playlists = getPlaylists()

        playlists.forEach { playlist ->
            val playlistId = playlist.playlistId
            val playlistName = playlist.playlistName
            uiPlaylists.add(UiPlaylists(playlistId, playlistName, getFourLargeCovers(playlistId)))
        }
        return uiPlaylists
    }


    @Transaction
    suspend fun getPlaylistBooks(key: Long): List<SlBookRelations>{

        val crossRefBookIdList: List<Long> = getPlaylistBookCrossRef(key)
        val slBookRelationList = mutableListOf<SlBookRelations>()

        crossRefBookIdList.forEach{id: Long ->
            val slbook = get(id)
            slBookRelationList.add(slbook)
        }

        return slBookRelationList
    }

    @Transaction
    fun clearAll(){
        clearSl()
        clearA()
        clearB()
        clearE()
    }

    @Query("SELECT * FROM sl_book_table")
    fun getAllBooks(): LiveData<List<SlBookRelations>>

    @Query("DELETE FROM sl_book_table")
    fun clearSl()
    @Query("DELETE FROM a_book_table")
    fun clearA()
    @Query("DELETE FROM e_book_table")
    fun clearE()
    @Query("DELETE FROM book_table")
    fun clearB()
    @Query("DELETE FROM playlist_sl_book_cross_ref WHERE playlist_id = :playlistId AND sl_book_id = :slBookId")
    fun removeBookFromPlaylist(playlistId: Long, slBookId: Long)
    @Query("DELETE FROM playlist_sl_book_cross_ref WHERE playlist_id = :playlistId")
    fun deletePlaylistFromCrossRef(playlistId: Long)
    @Query("DELETE FROM playlist_table WHERE playlist_id = :playlistId")
    fun deletePlaylistFromPlaylistTable(playlistId: Long)

    @Transaction
    fun deletePlaylistWithBooks(playlistId: Long){
        deletePlaylistFromCrossRef(playlistId)
        deletePlaylistFromPlaylistTable(playlistId)
    }
}