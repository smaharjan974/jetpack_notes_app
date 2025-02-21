package com.sundev.testnotes.feature_home.domain

import com.sundev.testnotes.core.data.local.toModel
import com.sundev.testnotes.core.domain.models.NoteModel
import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetNotesUseCase {
    private val repository : NotesRepositoryImpl = NotesRepositoryImpl.getInstance()

    suspend fun execute() : List<NoteModel> {
        return withContext(Dispatchers.IO) {
            repository.getAll().map {
                it.toModel()
            }
        }
    }

    companion object {
        private var _instance : GetNotesUseCase? = null

        fun getInstance() : GetNotesUseCase {
            if(_instance == null){
                _instance = GetNotesUseCase()
            }
            return _instance!!
        }
    }
}