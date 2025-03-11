package com.sundev.testnotes.feature_addNote.domain

import com.sundev.testnotes.core.domain.models.NoteModel
import com.sundev.testnotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddNoteUseCase(
    private val repository: NotesRepository
) {


    suspend fun execute(noteModel: NoteModel) {
        withContext(Dispatchers.IO) {
            if (noteModel.id == -1) {
                repository.insert(noteModel)
            } else {
                repository.update(noteModel)
            }
        }
    }

}