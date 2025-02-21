package com.sundev.testnotes.core.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sundev.testnotes.NotesApp.Companion.appContext

@Database(entities = [NoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao() : NotesDao

    companion object {

        private var _instance: AppDatabase? = null

        fun getInstance(): AppDatabase {
            if(_instance == null){
                _instance = Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).build()
            }
            return  _instance!!
        }
    }
}