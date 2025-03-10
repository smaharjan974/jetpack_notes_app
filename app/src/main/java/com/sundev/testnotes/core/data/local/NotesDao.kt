package com.sundev.testnotes.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun get(id: Int): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: NoteEntity):Long

    @Update
    fun update(item: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    fun deleteItem(id: Int)

}