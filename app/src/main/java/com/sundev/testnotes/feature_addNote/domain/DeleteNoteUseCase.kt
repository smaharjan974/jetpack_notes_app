package com.sundev.testnotes.feature_addNote.domain

import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import com.sundev.testnotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteNoteUseCase(
    private val repository: NotesRepository

) {

    suspend fun execute(id: Int) {
        withContext(Dispatchers.IO) {
            repository.delete(id)
        }
    }

}