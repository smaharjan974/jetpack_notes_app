package com.sundev.testnotes.feature_home.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.sundev.testnotes.core.data.local.FakeNotesDao
import com.sundev.testnotes.core.data.local.NoteEntity
import com.sundev.testnotes.core.data.local.toModel
import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import com.sundev.testnotes.core.domain.models.NoteModel
import com.sundev.testnotes.feature_addNote.domain.GetNoteUseCase
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


class GetNotesUseCaseTest {

    @get:Rule
    val instanceTaskExecutor = InstantTaskExecutorRule()
    
    private lateinit var items: ArrayList<NoteEntity>
    private lateinit var itemModels: List<NoteModel>

    private lateinit var fakeNotesDao: FakeNotesDao
    private lateinit var repository: NotesRepositoryImpl
    private lateinit var useCase: GetNoteUseCase

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
        useCase = GetNoteUseCase(
            repository = repository,
            ioDispatcher = UnconfinedTestDispatcher()
        )
    }
    
    @Test
    fun getAllNotes_return3Items() = runTest {
        // Given - setup the use case
        // already done above setup @before

        // when - get all the notes
        val result = repository.getAll()

        // Then - expecting 3 items
        Truth.assertThat(result.size).isEqualTo(itemModels.size)
        Truth.assertThat(result).isEqualTo(itemModels)


    }

    @Test
    fun getAllNotes_insertNewNote_return4Items() = runTest {
        // Given - setup the use case

        // when - insert a new note
        var newNote = note4.toModel()
        val newNoteId = repository.insert(newNote)
        newNote = newNote.copy(
            id = newNoteId
        )
        val result = repository.getAll()

        // Then - get all the results , expecting 4
        Truth.assertThat(result.size).isEqualTo(4)
    }

    @Test
    fun getAllNotes_delete1NewNote_return2Items() = runTest {
        // Given - setup the use case

        // when - insert a new note
        val noteId = note1.id ?: -1
        repository.delete(noteId)
        val result = repository.getAll()

            // Then - get all the results , expecting 4
        Truth.assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun getAllNotes_update1NewNote_return3Items() = runTest {
        // Given - setup the use case

        // when - insert a new note
        val updateNote = note1.toModel().copy(
            title = "Note Title updated"
        )
        repository.update(updateNote)
        val result = repository.getAll()

        // Then - get all the results , expecting 4
        Truth.assertThat(result.size).isEqualTo(3)
    }
    
}