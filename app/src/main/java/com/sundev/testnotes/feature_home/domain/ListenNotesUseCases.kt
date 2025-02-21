package com.sundev.testnotes.feature_home.domain

import com.sundev.testnotes.core.domain.models.NoteModel
import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListenNotesUseCases {

    private val repository: NotesRepositoryImpl = NotesRepositoryImpl.getInstance()

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

    companion object {
        private var _instance: ListenNotesUseCases? = null

        fun getInstance(): ListenNotesUseCases {
            if (_instance == null)
                _instance = ListenNotesUseCases()
            return _instance!!
        }
    }

}

sealed interface NotesEvent {
    data class Insert(val value: NoteModel) : NotesEvent
    data class Update(val value: NoteModel) : NotesEvent
    data class Delete(val value: Int) : NotesEvent
}