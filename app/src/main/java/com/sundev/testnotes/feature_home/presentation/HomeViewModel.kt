package com.sundev.testnotes.feature_home.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sundev.testnotes.Routes
import com.sundev.testnotes.feature_home.domain.GetNotesUseCase
import com.sundev.testnotes.feature_home.domain.ListenNotesUseCases
import com.sundev.testnotes.feature_home.domain.NotesEvent
import com.sundev.testnotes.core.domain.models.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val TAG = "HomeViewModel"

    private val getNotesUseCase: GetNotesUseCase = GetNotesUseCase.getInstance()
    private val listenNotesUseCases: ListenNotesUseCases = ListenNotesUseCases.getInstance()

    val noteList = mutableStateListOf<NoteModel>()

    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow: SharedFlow<HomeEvent> = _eventFlow.asSharedFlow()
    private val _scope = viewModelScope

    init {
        _scope.launch(Dispatchers.IO) {
            delay(1000L)
            val items = getNotesUseCase.execute()
            noteList.addAll(items)
        }

        _scope.launch(Dispatchers.IO) {
            listenNotesUseCases.execute().collect { event ->
                when(event){
                    is NotesEvent.Delete -> {
                        val itemIndex = noteList.indexOfFirst { it.id == event.value }
                        if(itemIndex != -1){
                            noteList.removeAt(itemIndex)
                        }
                    }
                    is NotesEvent.Insert -> noteList.add(0,event.value)
                    is NotesEvent.Update -> {
                        val itemIndex = noteList.indexOfFirst { it.id == event.value.id }
                        if (itemIndex != -1)
                            noteList[itemIndex] = event.value
                    }
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