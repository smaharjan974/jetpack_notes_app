package com.sundev.testnotes.addNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sundev.testnotes.models.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddNoteViewModel : ViewModel() {

    private var _title: MutableStateFlow<String> = MutableStateFlow<String>("")
    private var _description: MutableStateFlow<String> = MutableStateFlow<String>("")
    val title: StateFlow<String> = _title.asStateFlow()
    val description: StateFlow<String> = _description.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    fun titleOnValueChange(value: String){
        _title.value = value
    }

    fun descriptionOnValueChange(value: String){
        _description.value = value
    }

    fun backIconOnClick(){

        val noteModel = NoteModel(id = -1, title = _title.value, description = _description.value)

        // TODO saveNote

        // TODO Navigate Back
        viewModelScope.launch(Dispatchers.Main) {
            _event.emit(Event.NavigateBack(noteModel))
        }

    }

    sealed class Event {
        data class NavigateBack(val note: NoteModel): Event()
    }

}