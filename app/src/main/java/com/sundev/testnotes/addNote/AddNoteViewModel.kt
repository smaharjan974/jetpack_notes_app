package com.sundev.testnotes.addNote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sundev.testnotes.models.NoteModel
import com.sundev.testnotes.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddNoteViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository: NotesRepository = NotesRepository.getInstance()

    private var _title: MutableStateFlow<String> = MutableStateFlow<String>("")
    private var _description: MutableStateFlow<String> = MutableStateFlow<String>("")
    val title: StateFlow<String> = _title.asStateFlow()
    val description: StateFlow<String> = _description.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private var _noteId: Int = -1

    init {
        val noteId = savedStateHandle
            .get<Int>("id") ?: -1
        _noteId = noteId
        if(noteId != -1){
            val note = repository.get(noteId)
            _title.value = note.title
            _description.value = note.description
        }

    }

    fun titleOnValueChange(value: String){
        _title.value = value
    }

    fun descriptionOnValueChange(value: String){
        _description.value = value
    }

    fun backIconOnClick() = viewModelScope.launch(Dispatchers.IO){
        val noteModel = NoteModel(id = _noteId, title = _title.value, description = _description.value)
        val isNoteEmpty = noteModel.let {
            it.title.isEmpty() && it.description.isEmpty()
        }
        if(isNoteEmpty) return@launch
        // saveNote
        if(noteModel.id == -1){
            repository.insert(noteModel)
        }else{
            repository.update(noteModel)
        }

        // Navigate Back
        viewModelScope.launch(Dispatchers.Main) {
            _event.emit(Event.NavigateBack)
        }

    }

    sealed class Event {
        data object NavigateBack: Event()
    }

}