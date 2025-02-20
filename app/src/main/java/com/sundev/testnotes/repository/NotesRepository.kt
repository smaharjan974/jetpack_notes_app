package com.sundev.testnotes.repository

import com.sundev.testnotes.data.local.AppDatabase
import com.sundev.testnotes.data.local.NotesDao
import com.sundev.testnotes.data.local.toModel
import com.sundev.testnotes.models.NoteModel
import com.sundev.testnotes.models.toEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NotesRepository private constructor() {

    val dao: NotesDao = AppDatabase.getInstance().notesDao()

    companion object {
        private var _instance: NotesRepository? = null

        fun getInstance(): NotesRepository {
            if (_instance == null)
                _instance = NotesRepository()
            return _instance as NotesRepository
        }
    }

    private val _newNoteInsertionListener = MutableSharedFlow<NoteModel>()
    val newNoteInsertionListener: SharedFlow<NoteModel> = _newNoteInsertionListener.asSharedFlow()


    private val _updateNoteListener = MutableSharedFlow<NoteModel>()
    val updateNoteListener: SharedFlow<NoteModel> = _updateNoteListener.asSharedFlow()


    private val _deleteNoteListener = MutableSharedFlow<Int>()
    val deleteNoteListener: SharedFlow<Int> = _deleteNoteListener.asSharedFlow()


    suspend fun getAll(): List<NoteModel> {
        return dao.getAll().map {
            it.toModel()
        }
    }

    suspend fun get(id: Int): NoteModel {
        return dao.get(id).toModel()
    }

    suspend fun insert(item: NoteModel): Int {
        val newId = dao.insertItem(item.toEntity()).toInt()
        val newNote = item.copy(id = newId)
        _newNoteInsertionListener.emit(newNote)
        return newNote.id
    }

    suspend fun update(item: NoteModel) {
        dao.update(item.toEntity())
        _updateNoteListener.emit(item)
    }

    suspend fun delete(itemId: Int) {
        dao.deleteItem(itemId)
        _deleteNoteListener.emit(itemId)
    }
}