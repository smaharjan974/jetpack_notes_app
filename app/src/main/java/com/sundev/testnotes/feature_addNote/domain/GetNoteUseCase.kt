package com.sundev.testnotes.feature_addNote.domain

import com.sundev.testnotes.core.domain.models.NoteModel
import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import com.sundev.testnotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetNoteUseCase(

    private val repository: NotesRepository
) {

    suspend fun execute(id: Int): NoteModel {
        return withContext(Dispatchers.IO) {
            repository.get(id)
        }
    }

}