package com.storytel.booklibrary

import android.util.Log
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.storytel.booklibrary.data.*
import com.storytel.booklibrary.entities.*
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class SLBookDatabaseTest {

    private lateinit var slBookDao: SLBookDatabaseDao
    private lateinit var db: SLBookDatabase


    val aBook = ABookEntity(1, 1, "Bjarne Bendtsen", "10-01-2020")
    val aBook2 = ABookEntity(2, 1, "Lamseben Andersen", "10-01-2020")
    val eBook = EBookEntity(1, "10-01-2020", 1)
    val book = BookEntity(1, "Bjarke", 0, "/Images/123123", 2, "Jesper", "10-01-2020", 1)
    val slBook = SLBookEntity(502, 1, 2, book.bookId, aBook.aBookId, eBook.eBookid)
    val slBook2 = SLBookEntity(5122, 1, 2, book.bookId, aBook2.aBookId, eBook.eBookid)

    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        try {
            db = Room.inMemoryDatabaseBuilder(context, SLBookDatabase::class.java).allowMainThreadQueries().build()
        } catch (e: java.lang.Exception) {
            Timber.e(e)
        }

        slBookDao = db.slBookRoomDatabaseDao

    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
//        slBookDao.clear()
        db.close()
        Timber.i("Database closed")
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetSLBook() {


        slBookDao.insertSLBook(slBook)
        slBookDao.insertBook(book)
        slBookDao.insertEBook(eBook)
        slBookDao.insertABook(aBook)
        slBookDao.insertEntireBook(slBook2, book, aBook2, eBook)

//        val bookinfo = slBookDao.getAllBooks()[0].book.authorAsString
//        val bookinfo2 = slBookDao.getAllBooks()[0].slBook.restriction
//        val bookinfo3 = slBookDao.getAllBooks()[0].eBook.isComing
//        val bookinfo4 = slBookDao.getAllBooks()[0].aBook.narratorAsString

        val bookOnKey = slBookDao.get(502).aBook.narratorAsString


//        assertEquals("Bjarke", bookinfo)
//        assertEquals(1, bookinfo2)
//        assertEquals(1, bookinfo3)
//        assertEquals("Bjarne Bendtsen", bookinfo4)

        assertEquals("Bjarne Bendtsen", bookOnKey)

    }

//    @Test
//    @Throws(Exception::class)
//    fun playlistCrudTest() {
//
//        slBookDao.insertSLBook(slBook)
//        slBookDao.insertBook(book)
//        slBookDao.insertEBook(eBook)
//        slBookDao.insertABook(aBook)
//        slBookDao.insertEntireBook(slBook2, book, aBook2, eBook)
//
//        val playlist1 = PlaylistEntity(1, "træning")
//        slBookDao.insertPlaylistWithCrossRef(playlist1, slBook)
//
//        val i = slBookDao.getPlaylistWithSLBooks()[0].playlists[0].slbookId
//        val selBookFromPlaylist = slBookDao.get(i).aBook.narratorAsString
//        slBookDao.insertPlaylistRef(PlaylistSlBookCrossRef(playlist1.playlistId, slBook2.slbookId))
//
//        val playlistBooks = slBookDao.getPlaylistBooks(playlist1.playlistId)
//        val lamseben = playlistBooks[1].aBook.narratorAsString
//        val bjarne = playlistBooks[0].aBook.narratorAsString
//
//        assertEquals("Bjarne Bendtsen", selBookFromPlaylist)
//        assertEquals("træning", playlist1.playlistName)
//        assertEquals(2, playlistBooks.size)
//        assertEquals("Lamseben Andersen", lamseben)
//        assertEquals("Bjarne Bendtsen", bjarne)
//    }
}