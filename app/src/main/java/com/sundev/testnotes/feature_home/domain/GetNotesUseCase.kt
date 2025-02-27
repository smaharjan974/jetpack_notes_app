package com.sundev.testnotes.feature_home.domain

import com.sundev.testnotes.core.domain.models.NoteModel
import com.sundev.testnotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GetNotesUseCase constructor(private val repository: NotesRepository) {


    suspend fun execute(): List<NoteModel> {
        return withContext(Dispatchers.IO) {
            repository.getAll()
        }
    }
}