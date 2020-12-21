package com.storytel.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.stetho.Stetho
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.storytel.booklibrary.ui.fragments.BookLibraryFragment
import com.storytel.booklibrary.ui.fragments.LatestViewedFragment
import com.storytel.booklibrary.ui.fragments.PlaylistFragment
import com.storytel.booklibrary.ui.fragments.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class MainActivity: AppCompatActivity() {

    lateinit var bookLibraryFragment : BookLibraryFragment
    lateinit var playlistFragment: PlaylistFragment
    lateinit var searchFragment: SearchFragment
    lateinit var latestViewedFragment: LatestViewedFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            bookLibraryFragment = BookLibraryFragment.newInstance()
            supportFragmentManager
                    .beginTransaction().replace(R.id.frame_layout, bookLibraryFragment)
                    .addToBackStack("library")
                    .commit()
        }else{
            bookLibraryFragment = supportFragmentManager.findFragmentById(R.id.frame_layout) as BookLibraryFragment
        }

        Stetho.initializeWithDefaults(this)

        findViewById<BottomNavigationView>(R.id.btm_nav).apply {
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.bookshelf_btm_nav -> {
                        bookLibraryFragment = BookLibraryFragment.newInstance()
                        supportFragmentManager
                                .beginTransaction().replace(R.id.frame_layout, bookLibraryFragment)
                                .addToBackStack("library")
                                .commit()
                    }
                    R.id.playlists_btm_nav -> {
                        playlistFragment = PlaylistFragment.newInstance()
                        supportFragmentManager
                                .beginTransaction().replace(R.id.frame_layout, playlistFragment)
                                .addToBackStack("playlist")
                                .commit()
                    }
                    R.id.search_btm_nav -> {
                        searchFragment = SearchFragment.newInstance()
                        supportFragmentManager
                                .beginTransaction().replace(R.id.frame_layout, searchFragment)
                                .addToBackStack("search")
                                .commit()
                    }
                    R.id.latest_btm_nav -> {
                        latestViewedFragment = LatestViewedFragment.newInstance()
                        supportFragmentManager
                                .beginTransaction().replace(R.id.frame_layout, latestViewedFragment)
                                .addToBackStack("history")
                                .commit()
                    }
                }
                true
            }
        }
    }
}