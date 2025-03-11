package com.sundev.testnotes.feature_addNote.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.sundev.testnotes.DummyNotesEntity
import com.sundev.testnotes.core.data.local.FakeNotesDao
import com.sundev.testnotes.core.data.local.NoteEntity
import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import com.sundev.testnotes.core.domain.ListenNotesUseCases
import com.sundev.testnotes.core.domain.NotesEvent
import com.sundev.testnotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeleteNoteUseCaseTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NotesRepository
    private lateinit var fakeNotesDao: FakeNotesDao
    private lateinit var useCase: DeleteNoteUseCase
    private lateinit var listenNoteUseCase: ListenNotesUseCases

    private var testDispatcher = StandardTestDispatcher()

    @Before
    fun setupUseCase() {
        val items =
            arrayListOf(DummyNotesEntity.note1, DummyNotesEntity.note2, DummyNotesEntity.note3)
        fakeNotesDao = FakeNotesDao(items)
        repository = NotesRepositoryImpl(
            dao = fakeNotesDao,
        )
        useCase = DeleteNoteUseCase(
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
    fun deleteNote_provideValidNoteId_returnDeletedNoteId() = runTest(testDispatcher.scheduler) {
        // Given - setup the use case & listen note events
        var events = mutableListOf<NotesEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            listenNoteUseCase.execute().collect { newEvent ->
                events.add(newEvent)
            }
        }

        // When - Provide a Valid Note id to delete the note
        val noteId = DummyNotesEntity.note1.id ?: -1
        useCase.execute(noteId)
        val result = events.firstOrNull()?.let { it as NotesEvent.Delete }

        // Then - expected deleted note id
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.value).isEqualTo(noteId)
    }

    @Test
    fun deleteNote_provideInValidNoteId_returnDeletedNoteId() = runTest(testDispatcher.scheduler) {
        // Given - setup the use case & listen note events
        var events = mutableListOf<NotesEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            listenNoteUseCase.execute().collect { newEvent ->
                events.add(newEvent)
            }
        }

        // When - Provide a Valid Note id to delete the note
        val noteId = 30
        useCase.execute(noteId)
        val result = events.firstOrNull()?.let { it as NotesEvent.Delete }

        // Then - expected deleted note id
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.value).isEqualTo(noteId)
    }
}