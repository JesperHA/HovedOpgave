package com.storytel.booklibrary.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BookLibraryApi {
    @GET("/api/getBookShelf.action")
    @Headers("Accept: application/vnd.storytel.bookshelf-ids-v2+json")
    suspend fun getBookshelfMini(): Response<BookShelfMini>

    @GET("/api/getBookInfoForContent.action")
    suspend fun getBookDetails(@Query("bookId") bookId: Long): Response<BookInfoForContent>
}