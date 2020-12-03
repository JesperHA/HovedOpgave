package com.storytel.booklibrary.data

import com.storytel.base.util.ApiResponse
import com.storytel.base.util.ApiSuccess
import com.storytel.booklibrary.api.*
import com.storytel.booklibrary.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class BookLibraryRepository @Inject constructor(private val bookLibraryApi: BookLibraryApi, private val slBookDao: SLBookDatabaseDao) {

    fun getAllSlBooks() = slBookDao.getAllBooks()
    fun getAllPlaylistEntities() = slBookDao.getPlaylistLiveData()
    suspend fun getAllSearchBooks(input: String) = slBookDao.getSearchResults(input)
    suspend fun getAllPlaylists() = slBookDao.getPlaylistsWithCovers()

    suspend fun getPlaylist(playlistId: Long) = withContext(Dispatchers.IO) { slBookDao.getPlaylistBooks(playlistId) }
    suspend fun printOrder(playlistId: Long) = withContext(Dispatchers.IO) { slBookDao.printOrder(playlistId) }

    fun insertPlaylist(playlistName: String) = slBookDao.insertPlaylist(PlaylistEntity(0, playlistName))
    fun insertToPlaylist(playlistId: Long, slBookId: Long) = slBookDao.insertToPlaylist(playlistId, slBookId)
    fun removeBookFromPlaylist(playlistId: Long, slBookId: Long) = slBookDao.removeBookFromPlaylist(playlistId, slBookId)
    fun deletePlaylist(playlistId: Long) = slBookDao.deletePlaylistWithBooks(playlistId)
    fun updatePlaylist(playlistId: Long, playlistName: String) = slBookDao.updatePlaylist(playlistId, playlistName)
    fun insertPlaylistAndSlBook(slBookId: Long, playlistName: String) = slBookDao.insertPlaylistWithCrossRef(slBookId, playlistName)
    suspend fun updatePlaylistOrder(from: Int, to: Int, playlistId: Long) = withContext(Dispatchers.IO) {slBookDao.updatePlaylistOrder(from, to, playlistId)}




    private suspend fun clearDB() = withContext(Dispatchers.IO) {
        slBookDao.clearAll()
    }

    suspend fun fetchBookshelf(): ApiResponse<BookShelfMini> {
        clearDB()
        return try {
            val apiResponse = ApiResponse.create(bookLibraryApi.getBookshelfMini())
            Timber.i("fetched book")
            if (apiResponse is ApiSuccess) {
                withContext(Dispatchers.IO) { saveResponseToDatabase(apiResponse.body) }
            }
            apiResponse
        } catch (e: IOException) {
            Timber.e(e)
            ApiResponse.create(e)
        }
    }

    suspend fun fetchBookDetails(bookId: Long): BookInfoWrapper? {
//        lateinit var bookInfo: BookInfoWrapper
        try {
            val apiResponse = ApiResponse.create(bookLibraryApi.getBookDetails(bookId))

            if (apiResponse is ApiSuccess){
                return withContext(Dispatchers.IO) { getBookDetails(apiResponse.body) }
            }

        } catch (e: IOException){
            Timber.e(e)

        }
        return null
//        return bookInfo
    }

    fun getBookDetails(bookinfo: BookInfoForContent): BookInfoWrapper? {

        val a = bookinfo.slb?.abook
        val b = bookinfo.slb?.ebook
        val c = bookinfo.slb?.book

        val aBook = ABook(a?.description ?: "", a?.lengthInHHMM ?: "", a?.narratorAsString ?: "",
                            a?.publisher ?: Publisher(-1L, ""), a?.releaseDateFormat ?: "" )
        val eBook = EBook(b?.description ?: "", b?.publisher ?: Publisher(-1L, ""), b?.releaseDateFormat ?: "")
        val book = Book(c?.authorsAsString ?: "", c?.category ?: Category(-1L, ""), c?.cover ?: "",
                        c?.coverE ?: "", c?.grade ?: -1F, c?.language ?: Language(-1L, "", ""),
                        c?.largeCover ?: "", c?.largecoverE ?: "", c?.name ?: "", c?.nrGrade ?: -1, c?.smallCover ?: "",
                        c?.translatorAsString ?: "")

        return BookInfoWrapper(aBook, eBook, book)
    }

    private fun saveResponseToDatabase(bookShelfMini: BookShelfMini) {

        fun insertBooks(list: List<SLBookMini>) {

            val entityWrapperList = mutableListOf<BookEntityWrapper>()

            list.forEach { slBookMini: SLBookMini ->
                val a = slBookMini.abook
                val b = slBookMini.ebook
                val c = slBookMini.book
                val d = slBookMini
                val aBook = ABookEntity(a?.id ?: -1, a?.isComing ?: -1,
                        a?.narratorAsString ?: "", a?.releaseDateFormat ?: "")
                val eBook = EBookEntity(b?.id ?: -1, b?.releaseDateFormat ?: "", b?.isComing
                        ?: -1)
                val book = BookEntity(c?.id ?: -1, c?.authorAsString ?: "", c?.haveRead
                        ?: -1,
                        c?.largeCover ?: "", c?.mappingStatus ?: -1, c?.name
                        ?: "", c?.releaseDateString ?: "",
                        c?.seriesOrder ?: -1)
                val slBook = SLBookEntity(d.id ?: -1, d.restriction ?: -1, d.status
                        ?: -1, c?.id ?: -1,
                        a?.id ?: -1, b?.id ?: -1)

                entityWrapperList.add(BookEntityWrapper(slBook, book, aBook, eBook))
            }
            slBookDao.insertBooksFromApi(entityWrapperList)
        }
        insertBooks(bookShelfMini.books)
        Timber.d("success - save to db")
    }
}

data class BookInfoWrapper(val aBook: ABook?, val eBook: EBook?, val book: Book)

data class BookEntityWrapper(val slBook: SLBookEntity, val book: BookEntity, val aBook: ABookEntity?, val eBook: EBookEntity?)

data class UiPlaylists(val playlistId: Long, val playlistName: String, val urls: List<String>)