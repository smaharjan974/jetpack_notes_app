package com.sundev.testnotes.core.domain.repository

import com.sundev.testnotes.core.data.local.NoteEntity
import com.sundev.testnotes.core.domain.models.NoteModel
import kotlinx.coroutines.flow.SharedFlow

interface NotesRepository {
    val newNoteInsertionListener: SharedFlow<NoteModel>
    val updateNoteListener: SharedFlow<NoteModel>
    val deleteNoteListener: SharedFlow<Int>

    suspend fun getAll(): List<NoteModel>

    suspend fun get(id: Int): NoteModel

    suspend fun insert(item: NoteModel): Int

    suspend fun update(item: NoteModel)

    suspend fun delete(itemId: Int)
}