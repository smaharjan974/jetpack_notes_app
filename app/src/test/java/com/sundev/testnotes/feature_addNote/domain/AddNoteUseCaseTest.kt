package com.sundev.testnotes.feature_addNote.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.sundev.testnotes.core.data.local.FakeNotesDao
import com.sundev.testnotes.core.data.local.NoteEntity
import com.sundev.testnotes.core.data.local.toModel
import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import com.sundev.testnotes.core.domain.ListenNotesUseCases
import com.sundev.testnotes.core.domain.NotesEvent
import com.sundev.testnotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

private val note1 = NoteEntity(1, "Note 1", "My first note")
private val note2 = NoteEntity(2, "Note 2", "My Second note")
private val note3 = NoteEntity(3, "Note 3", "My third note")
private val note4 = NoteEntity(null, "Note 4", "My Fourth note")
private val note5 = NoteEntity(5, "Note 5", "My 5th note")

class AddNoteUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NotesRepository
    private lateinit var fakeNotesDao: FakeNotesDao
    private lateinit var useCase: AddNoteUseCase
    private lateinit var listenNoteUseCase: ListenNotesUseCases

    private var testDispatcher = StandardTestDispatcher()

    @Before
    fun setupUseCase() {
        val items = arrayListOf(note1, note2, note3)
        fakeNotesDao = FakeNotesDao(items)
        repository = NotesRepositoryImpl(
            dao = fakeNotesDao,
        )
        useCase = AddNoteUseCase(
            repository = repository,
            ioDispatcher = UnconfinedTestDispatcher(testDispatcher.scheduler)
        )
        listenNoteUseCase =
            ListenNotesUseCases(
                repository = repository,
                ioDispatcher = UnconfinedTestDispatcher(testDispatcher.scheduler)
            )
    }

    @Test
    fun addNote_insertNewNote_returnNewInsertedNote() = runTest {
        // Give - setup the use case & listen the note events
        var events = mutableListOf<NotesEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            listenNoteUseCase.execute().collect { newEvent ->
                events.add(newEvent)
            }
        }


        //When - insert a New Note
        var newNote = note4.toModel()
        useCase.execute(newNote)
        val result = events.firstOrNull()?.let { it as NotesEvent.Insert }

        // Then - expected New inserted note
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.javaClass?.simpleName).isEqualTo(NotesEvent.Insert::class.java.simpleName)
        newNote = newNote.copy(
            id = result?.value?.id ?: -1
        )
        Truth.assertThat(result?.value).isEqualTo(newNote)
    }

    @Test
    fun addNote_UpdatedNewNote_returnNewUpdatedNote() = runTest {
        // Give - setup the use case & listen the note events
        var events = mutableListOf<NotesEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            listenNoteUseCase.execute().collect { newEvent ->
                events.add(newEvent)
            }
        }


        //When - insert a New Note
        var newNote = note1.toModel().copy(
            title = "First Note updated"
        )
        useCase.execute(newNote)
        val result = events.firstOrNull()?.let { it as NotesEvent.Update }

        // Then - expected New inserted note
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.javaClass?.simpleName).isEqualTo(NotesEvent.Update::class.java.simpleName)
        Truth.assertThat(result?.value).isEqualTo(newNote)
    }

    @Test
    fun addNote_UpdatedAWhichNoteAlreadyExist_returnNewUpdatedNote() = runTest {
        // Give - setup the use case & listen the note events
        var events = mutableListOf<NotesEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            listenNoteUseCase.execute().collect { newEvent ->
                events.add(newEvent)
            }
        }


        //When - Update the note which not already exist in a repo.
        var newNote = note5.toModel().copy(
            title = "First Note updated"
        )
        useCase.execute(newNote)
        val result = events.firstOrNull()?.let { it as NotesEvent.Update }

        // Then - expected New inserted note
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.javaClass?.simpleName).isEqualTo(NotesEvent.Update::class.java.simpleName)
        Truth.assertThat(result?.value).isEqualTo(newNote)
    }

}