package com.sundev.testnotes.feature_home.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sundev.testnotes.Routes
import com.sundev.testnotes.core.di.IoDispatcher
import com.sundev.testnotes.core.di.MainDispatcher
import com.sundev.testnotes.feature_home.domain.GetNotesUseCase
import com.sundev.testnotes.core.domain.ListenNotesUseCases
import com.sundev.testnotes.core.domain.NotesEvent
import com.sundev.testnotes.core.domain.models.NoteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val listenNotesUseCases: ListenNotesUseCases,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val TAG = "HomeViewModel"


    val noteList = mutableStateListOf<NoteModel>()

    private val _eventFlow = MutableSharedFlow<HomeEvent>()
    val eventFlow: SharedFlow<HomeEvent> = _eventFlow.asSharedFlow()
    private val _scope = viewModelScope

    init {
        getNotes()

        _scope.launch(ioDispatcher) {
            listenNotesUseCases.execute().collect { event ->
                when (event) {
                    is NotesEvent.Delete -> {
                        val itemIndex = noteList.indexOfFirst { it.id == event.value }
                        if (itemIndex != -1) {
                            noteList.removeAt(itemIndex)
                        }
                    }

                    is NotesEvent.Insert -> noteList.add(0, event.value)
                    is NotesEvent.Update -> {
                        val itemIndex = noteList.indexOfFirst { it.id == event.value.id }
                        if (itemIndex != -1)
                            noteList[itemIndex] = event.value
                    }
                }
            }
        }
    }

    private fun getNotes() {
        _scope.launch(ioDispatcher) {
            delay(1000L)
            val items = getNotesUseCase.execute()
            noteList.addAll(items)
        }
    }

    fun action(action: HomeAction) {
        when (action) {
            HomeAction.AddNewNote -> addNewNote()
            is HomeAction.ListItemOnClick -> listItemOnClick(action.value)
        }
    }

    private fun addNewNote() = _scope.launch {
        val route = Routes.ADD_NOTE + "/-1"
        _eventFlow.emit(HomeEvent.NavigateNext(route))
    }

    private fun listItemOnClick(id: Int) = _scope.launch(mainDispatcher) {
        val route = Routes.ADD_NOTE + "/$id"
        _eventFlow.emit(HomeEvent.NavigateNext(route))
    }
}