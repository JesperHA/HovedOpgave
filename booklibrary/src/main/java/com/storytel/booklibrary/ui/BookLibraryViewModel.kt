package com.storytel.booklibrary.ui

import androidx.lifecycle.*
import com.storytel.booklibrary.data.BookInfoWrapper
import com.storytel.booklibrary.data.BookLibraryRepository
import com.storytel.booklibrary.data.UiPlaylists
import com.storytel.booklibrary.entities.HistoryEntity
import com.storytel.booklibrary.entities.SlBookRelations
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FragmentScoped
class BookLibraryViewModel @Inject constructor(
        private val bookLibraryRepository: BookLibraryRepository) : ViewModel() {

    val slBooks = bookLibraryRepository.getAllSlBooks()
    val bookDetails = MutableLiveData<BookInfoWrapper>()

    val playlistEntities = bookLibraryRepository.getAllPlaylistEntities()
    val uiPlaylists: LiveData<List<UiPlaylists>>

    private val playlistId = MutableLiveData<Long>()
    val playlistBooks: LiveData<List<SlBookRelations>>

    private val searchField = MutableLiveData<String>()
    val searchFieldShow: LiveData<List<SlBookRelations>>


//    val history = bookLibraryRepository.fetchHistory()
    private val historyEntities = bookLibraryRepository.fetchHistoryEntities()
    val history: LiveData<List<SlBookRelations>>


//    fun fetchHistory() {
//        viewModelScope.launch(Dispatchers.IO) {
//            history.value = bookLibraryRepository.fetchHistory()
//        }
//    }

    fun moveItem(from: Int, to: Int, playlistId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            bookLibraryRepository.updatePlaylistOrder(from, to, playlistId)
        }
    }


    //Test function to retrieve indexed order
    fun printOrder(playlistId: Long){
        viewModelScope.launch(Dispatchers.IO){
            bookLibraryRepository.printOrder(playlistId)

        }
    }

    fun updatePlaylist(playlistId: Long, playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            bookLibraryRepository.updatePlaylist(playlistId, playlistName)
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            bookLibraryRepository.deletePlaylist(playlistId)
        }
    }

    fun removeBookFromPlaylist(playlistId: Long, slBookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            bookLibraryRepository.removeBookFromPlaylist(playlistId, slBookId)
        }
    }

    fun insertToPlaylist(playlistId: Long, slBookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            bookLibraryRepository.insertToPlaylist(playlistId, slBookId)
        }
    }

    fun insertPlaylistAndSlBook(slBookId: Long, playlistName: String) {
        if (slBookId != -1L) {
            viewModelScope.launch(Dispatchers.IO) {
                bookLibraryRepository.insertPlaylistAndSlBook(slBookId, playlistName)
            }
        } else {
            insertPlaylist(playlistName)
        }
    }

    fun insertPlaylist(playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            bookLibraryRepository.insertPlaylist(playlistName)
        }

    }

    fun addToHistory(slBookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            bookLibraryRepository.addToHistory(slBookId)
        }
    }

    init {

        if (!hasFetched) {
            fetchBookshelf()
            hasFetched = true
        }

        playlistBooks = playlistId.switchMap { id ->
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(bookLibraryRepository.getPlaylist(id))
            }
        }

        uiPlaylists = playlistEntities.switchMap {
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(bookLibraryRepository.getAllPlaylists())
            }
        }

        searchFieldShow = searchField.switchMap {input ->
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(bookLibraryRepository.getAllSearchBooks(input))
            }
        }

        history = historyEntities.switchMap {
            liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
                emit(bookLibraryRepository.fetchHistory(it))
            }
        }

        //        getPlaylists()
    }


//    fun getPlaylists(){
//        viewModelScope.launch {
//            val uiPlaylists = withContext(Dispatchers.IO){
//                bookLibraryRepository.getAllPlaylists()
//            }
//            _uiPlaylist.value = uiPlaylists
//        }
//    }

    fun setSearchField(value: String){
        viewModelScope.launch {
            searchField.value = value
        }
    }





    fun loadPlaylist(id: Long) {
        playlistId.value = id
    }

    fun fetchBookshelf() {
        viewModelScope.launch {
            bookLibraryRepository.fetchBookshelf()
//            Timber.i("Books fetched")
        }
    }

    fun fetchBookDetails(bookId: Long){
        viewModelScope.launch{
            bookDetails.value = bookLibraryRepository.fetchBookDetails(bookId)
        }
    }

//    fun fetchHistory(){
//            history.value = bookLibraryRepository.fetchHistory()
//
//    }

}

var hasFetched = false