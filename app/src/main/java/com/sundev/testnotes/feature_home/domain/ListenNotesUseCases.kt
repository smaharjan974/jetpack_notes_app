package com.sundev.testnotes.feature_home.domain

import com.sundev.testnotes.core.domain.models.NoteModel
import com.sundev.testnotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListenNotesUseCases constructor(
    private val repository: NotesRepository
) {


    suspend fun execute(): Flow<NotesEvent> {
        return channelFlow {
            withContext(Dispatchers.IO) {
                launch {
                    repository.newNoteInsertionListener.collect { newNote ->
                        send(NotesEvent.Insert(newNote))
                    }
                }

                launch {
                    repository.updateNoteListener.collect { updateNote ->
                        send(NotesEvent.Update(updateNote))
                    }
                }

                launch {
                    repository.deleteNoteListener.collect { id ->
                        send(NotesEvent.Delete(id))
                    }
                }

            }

        }

    }
}

sealed interface NotesEvent {
    data class Insert(val value: NoteModel) : NotesEvent
    data class Update(val value: NoteModel) : NotesEvent
    data class Delete(val value: Int) : NotesEvent
}