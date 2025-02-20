package com.sundev.testnotes.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sundev.testnotes.Routes
import com.sundev.testnotes.models.NoteModel
import com.sundev.testnotes.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val TAG = "HomeViewModel"

    private val _repository: NotesRepository = NotesRepository.getInstance()

    val noteList = mutableStateListOf<NoteModel>()

    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow: SharedFlow<HomeEvent> = _eventFlow.asSharedFlow()
    private val _scope = viewModelScope

    init {
        val items = _repository.getAll()
        noteList.addAll(items)

        _scope.launch {
            _repository.newNoteInsertionListener.collect { newNote ->
                noteList.add(newNote)
            }
        }

        _scope.launch {
            _repository.updateNoteListener.collect { updatedNote ->
                val itemIndex = noteList.indexOfFirst { it.id == updatedNote.id }
                if (itemIndex != -1)
                    noteList[itemIndex] = updatedNote
            }
        }

        _scope.launch {
            _repository.deleteNoteListener.collect { id ->
               val itemIndex = noteList.indexOfFirst { it.id == id }
                if(itemIndex != -1){
                    noteList.removeAt(itemIndex)
                }
            }
        }
    }


    fun listItemOnClick(id: Int) = _scope.launch(Dispatchers.Main) {
        Log.d(TAG, "listItemOnClick: $id")
        val route = Routes.ADD_NOTE + "/$id"
        _eventFlow.emit(HomeEvent.NavigateNext(route))
    }

    sealed class HomeEvent {
        data class NavigateNext(val route: String) : HomeEvent()
    }

}