package com.storytel.booklibrary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.storytel.booklibrary.BuildConfig
import com.storytel.booklibrary.entities.*

@Database(entities = [SLBookEntity::class, EBookEntity::class, ABookEntity::class,
    BookEntity::class, PlaylistEntity::class, PlaylistSlBookCrossRef::class], version = 1, exportSchema = false)
abstract class SLBookDatabase : RoomDatabase() {

    abstract val slBookRoomDatabaseDao: SLBookDatabaseDao

    companion object {

        fun provideDb(context: Context): SLBookDatabase {
            var b: RoomDatabase.Builder<SLBookDatabase> =
                    Room.databaseBuilder(context, SLBookDatabase::class.java, "book_database.db")
            if (!BuildConfig.DEBUG) {
                b = b.fallbackToDestructiveMigration()
            }
            return b.build()

        }
    }
}