package com.storytel.booklibrary.api

import androidx.annotation.Keep

@Keep data class BookIdList(val books: List<BookId>)
@Keep data class BookId(val id: Int)
@Keep data class UpdatedBookmarksResponse(val bookmarks: List<Position?>)
@Keep data class Position(val bookId: Int, val pos: Long, val insertDate: String?, val kidsMode: Boolean,
                          val type: Int)

@Keep data class BookShelfMini(val books: List<SLBookMini>)
@Keep data class SLBookMini(val id: Long?, val restriction: Int?, val status: Int?, val book: BookMini?, val abook: ABookTypeMini?,
                            val ebook: EBookTypeMini?)

@Keep data class BookMini(val authorAsString: String?, val haveRead: Int?, val id: Long?, val largeCover: String?, val mappingStatus: Int?,
                            val name: String?, val releaseDateString: String?, val seriesOrder: Int?)
@Keep data class ABookTypeMini(val id: Long?, val isComing: Int?, val narratorAsString: String?, val releaseDateFormat: String?)
@Keep data class EBookTypeMini(val id: Long?, val releaseDateFormat: String?, val isComing: Int?)


// bookinfo content
@Keep data class BookInfoForContent(val bookId: Long?, val slb: SLB? )
@Keep data class SLB(val abook: ABook?, val ebook: EBook?, val book: Book)
@Keep data class ABook(val description: String?, val lengthInHHMM: String?, val narratorAsString: String?,
                       val publisher: Publisher?, val releaseDateFormat: String?)
@Keep data class EBook(val description: String?, val publisher: Publisher?, val releaseDateFormat: String?)
@Keep data class Book(val authorsAsString: String?, val category: Category?, val cover: String?, val coverE: String?,
                      val grade: Float, val language: Language, val largeCover: String?, val largecoverE: String?,
                      val name: String?, val nrGrade: Int, val smallCover: String?, val translatorAsString: String?)

@Keep data class Language(val id: Long, val localizedName: String?, val name: String?)
@Keep data class Category(val id: Long, val title: String?)
@Keep data class Publisher(val id: Long, val name: String?)

