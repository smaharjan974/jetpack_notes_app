package com.sundev.testnotes.feature_addNote.domain

import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import com.sundev.testnotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteNoteUseCase(
    private val repository: NotesRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

) {

    suspend fun execute(id: Int) {
        withContext(ioDispatcher) {
            repository.delete(id)
        }
    }

}