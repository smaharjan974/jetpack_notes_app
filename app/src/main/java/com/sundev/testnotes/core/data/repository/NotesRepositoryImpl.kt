package com.sundev.testnotes.core.data.repository

import com.sundev.testnotes.core.data.local.AppDatabase
import com.sundev.testnotes.core.data.local.NoteEntity
import com.sundev.testnotes.core.data.local.NotesDao
import com.sundev.testnotes.core.data.local.toModel
import com.sundev.testnotes.core.domain.models.NoteModel
import com.sundev.testnotes.core.domain.models.toEntity
import com.sundev.testnotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NotesRepositoryImpl private constructor() : NotesRepository {

    val dao: NotesDao = AppDatabase.getInstance().notesDao()

    companion object {
        private var _instance: NotesRepositoryImpl? = null

        fun getInstance(): NotesRepositoryImpl {
            if (_instance == null)
                _instance = NotesRepositoryImpl()
            return _instance as NotesRepositoryImpl
        }
    }

    private val _newNoteInsertionListener = MutableSharedFlow<NoteModel>()
    override val newNoteInsertionListener: SharedFlow<NoteModel> = _newNoteInsertionListener.asSharedFlow()


    private val _updateNoteListener = MutableSharedFlow<NoteModel>()
    override val updateNoteListener: SharedFlow<NoteModel> = _updateNoteListener.asSharedFlow()


    private val _deleteNoteListener = MutableSharedFlow<Int>()
    override val deleteNoteListener: SharedFlow<Int> = _deleteNoteListener.asSharedFlow()


    override suspend fun getAll(): List<NoteEntity> {
        return dao.getAll()
    }

    override suspend fun get(id: Int): NoteModel {
        return dao.get(id).toModel()
    }

    override suspend fun insert(item: NoteModel): Int {
        val newId = dao.insertItem(item.toEntity()).toInt()
        val newNote = item.copy(id = newId)
        _newNoteInsertionListener.emit(newNote)
        return newNote.id
    }

    override suspend fun update(item: NoteModel) {
        dao.update(item.toEntity())
        _updateNoteListener.emit(item)
    }

    override suspend fun delete(itemId: Int) {
        dao.deleteItem(itemId)
        _deleteNoteListener.emit(itemId)
    }
}