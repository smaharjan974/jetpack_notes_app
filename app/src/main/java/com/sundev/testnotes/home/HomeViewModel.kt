package com.sundev.testnotes.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.sundev.testnotes.models.NoteModel
import com.sundev.testnotes.models.dummyNotes

class HomeViewModel : ViewModel() {
    
    private val TAG = "HomeViewModel"

    val notes = mutableStateListOf<NoteModel>().apply {
        addAll(dummyNotes())
    }


    fun listItemOnClick(id: Int){
        Log.d(TAG, "listItemOnClick: $id")
    }
    
    fun addNewNote() {
        Log.d(TAG, "addNewNote: ")
    }

    fun saveNote(newNoteObj: NoteModel){
        Log.d(TAG, "saveNote: $newNoteObj")
        notes.add(newNoteObj)
    }

}