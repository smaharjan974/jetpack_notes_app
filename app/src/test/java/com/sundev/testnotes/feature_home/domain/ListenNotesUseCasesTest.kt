package com.sundev.testnotes.feature_home.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.sundev.testnotes.core.data.local.FakeNotesDao
import com.sundev.testnotes.core.data.local.NoteEntity
import com.sundev.testnotes.core.data.local.toModel
import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import com.sundev.testnotes.core.domain.models.NoteModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val note1 = NoteEntity(1, "Note 1", "My first note")
private val note2 = NoteEntity(2, "Note 2", "My Second note")
private val note3 = NoteEntity(3, "Note 3", "My third note")
private val note4 = NoteEntity(null, "Note 4", "My Fourth note")
private val note5 = NoteEntity(5, "Note 5", "My 5th note")

class ListenNotesUseCasesTest {

    @get:Rule
    val instanceTaskExecutor = InstantTaskExecutorRule()

    private lateinit var items: ArrayList<NoteEntity>
    private lateinit var itemModels: List<NoteModel>

    private lateinit var fakeNotesDao: FakeNotesDao
    private lateinit var repository: NotesRepositoryImpl
    private lateinit var useCase: ListenNotesUseCases

    private var testDispatcher = StandardTestDispatcher()

    @Before
    fun setupUseCase() {
        items = arrayListOf(
            note1,
            note2,
            note3
        )
        itemModels = items.map { it.toModel() }
        fakeNotesDao = FakeNotesDao(items)
        repository = NotesRepositoryImpl(fakeNotesDao)
        useCase = ListenNotesUseCases(
            repository = repository,
            ioDispatcher = UnconfinedTestDispatcher(testDispatcher.scheduler)
        )
    }


    @Test
    fun listenNotes_insertANewNote_returnInsertNoteEvent() = runTest(testDispatcher.scheduler) {
        // Given - start listing the events
        var events = mutableListOf<NotesEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            useCase.execute().collect { newEvent ->
                events.add(newEvent)
            }
        }

        // When - add a new Note
        var newNote = note4.toModel()
        val noteId = repository.insert(newNote)
        newNote = newNote.copy(
            id = noteId
        )
        val result = events.firstOrNull()

        // Then = expected insert New Event
        Truth.assertThat((result as NotesEvent.Insert).javaClass.simpleName)
            .isEqualTo(NotesEvent.Insert::class.java.simpleName)
        Truth.assertThat((result as NotesEvent.Insert).value).isEqualTo(newNote)
    }

    @Test
    fun listenNotes_updateANewNote_returnUpdateNoteEvent() = runTest(testDispatcher.scheduler) {
        // Given - start listing the events
        var events = mutableListOf<NotesEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            useCase.execute().collect { newEvent ->
                events.add(newEvent)
            }
        }

        // When - update one note
        val updatedNote = note1.toModel().copy(
            title = "Note Title updated"
        )
        repository.update(updatedNote)
        val result = events.firstOrNull()?.let { it as NotesEvent.Update }

        // Then - expecting Update note event
        Truth.assertThat(result?.javaClass?.simpleName).isEqualTo(NotesEvent.Update::class.java.simpleName)
        Truth.assertThat(result?.value).isEqualTo(updatedNote)
    }

    @Test
    fun listenNotes_deleteANewNote_returnDeleteNoteEvent() = runTest(testDispatcher.scheduler) {
        // Given - start listing the events
        var events = mutableListOf<NotesEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            useCase.execute().collect { newEvent ->
                events.add(newEvent)
            }
        }

        // When - Delete one note
        val noteId = note1.id ?: -1
        repository.delete(noteId)
        val result = events.firstOrNull()?.let { it as NotesEvent.Delete }

        // Then - expecting Update note event
        Truth.assertThat(result?.javaClass?.simpleName).isEqualTo(NotesEvent.Delete::class.java.simpleName)
        Truth.assertThat(result?.value).isEqualTo(noteId)
    }



}