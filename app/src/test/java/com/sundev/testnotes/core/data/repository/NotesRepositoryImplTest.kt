@file:OptIn(ExperimentalCoroutinesApi::class)

package com.sundev.testnotes.core.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.sundev.testnotes.core.data.local.FakeNotesDao
import com.sundev.testnotes.core.data.local.NoteEntity
import com.sundev.testnotes.core.data.local.toModel
import com.sundev.testnotes.core.domain.models.NoteModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.pow

val note1 = NoteEntity(1, "Note 1", "My first note")
val note2 = NoteEntity(2, "Note 2", "My Second note")
val note3 = NoteEntity(3, "Note 3", "My third note")
val note4 = NoteEntity(null, "Note 4", "My Fourth note")
val note5 = NoteEntity(5, "Note 5", "My 5th note")

class NotesRepositoryImplTest {

    @get:Rule
    val instanceTaskExecutor = InstantTaskExecutorRule()

    private lateinit var fakeNotesDao: FakeNotesDao
    private lateinit var repository: NotesRepositoryImpl

    @Before
    fun setupRepository() {
        val allItems = arrayListOf(note1, note2, note3)
        fakeNotesDao = FakeNotesDao(allItems)
        repository = NotesRepositoryImpl(fakeNotesDao)
    }

    @Test
    fun getAll_return3Items() = runTest {

        // When - get all the notes
        val result = repository.getAll()

        // Then - expected 3 items
        Truth.assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun getAll_deleteANote_return2Items() = runTest {

        // when - delete a note & get all the notes
        val noteId = note3.id ?: 0
        repository.delete(noteId)
        val result = repository.getAll()

        // Then - expected 2 items
        Truth.assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun getAll_insertNewNote_return4Items() = runTest {

        // when - delete a note & get all the notes
        val newNoteModel = note4.toModel()
        repository.insert(newNoteModel)
        val result = repository.getAll()

        // Then - expected 2 items
        Truth.assertThat(result.size).isEqualTo(4)
    }

    @Test
    fun getNote_returnOneItem() = runTest {

        // when - get first Note
        val note1Model = note1.toModel()
        val result = repository.get(note1Model.id)

        // Then - correct note in response
        Truth.assertThat(result).isEqualTo(note1Model)
    }

    @Test
    fun getNote_passInvalidNoteId_returnOneItem() = runTest {

        // when - get first Note
        val noteId = 25
        val result = repository.get(noteId)

        // Then - null in response
        Truth.assertThat(result).isEqualTo(null)
    }

    @Test
    fun insert_newNote_returnOneItem() = runTest {

        // when - get first Note
        val newNoteModel = note4.toModel()
        val newNoteId = repository.insert(newNoteModel)
        val updatedNoteModel = newNoteModel.copy(
            id = newNoteId
        )
        val result = repository.get(newNoteId)

        // Then - null in response
        Truth.assertThat(result).isEqualTo(updatedNoteModel)
    }

    @Test
    fun insertNote_existingNotesAsInsertNote_returnOneItem() = runTest {

        //Given - use existing note to insert
        val note1Model = note1.toModel()

        // when - insert the note
        repository.insert(note1Model)
        val result = repository.get(note1Model.id)

        // Then - expecting one item
        Truth.assertThat(result).isEqualTo(note1Model)
    }

    @Test
    fun insertNote_passANewNoteWithIdWithDoesNotExistInDB_returnOneItem() = runTest {

        //Given - use existing note to insert
        val newNoteModel = note5.toModel()

        // when - insert the note
        val newNoteId = repository.insert(newNoteModel)
        val updateNoteModel = newNoteModel.copy(
            id = newNoteId
        )
        val result = repository.get(newNoteId)

        // Then - expecting one item
        Truth.assertThat(result).isEqualTo(updateNoteModel)
    }

    @Test
    fun updateNote_returnUpdatedNoteItem() = runTest {

        //Given - use existing note to insert
        val updateNote = note1.copy(
            title = "This is update title"
        ).toModel()

        // when - insert the note
        repository.update(updateNote)
        val result = repository.get(updateNote.id)

        // Then - expecting one item
        Truth.assertThat(result).isEqualTo(updateNote)
    }


    @Test
    fun deleteNote_returnNull() = runTest {

        //Given - use existing note to insert
        val noteId = note1.id ?: -1

        // when - insert the note
        repository.delete(noteId)
        val result = repository.get(noteId)

        // Then - expecting one item
        Truth.assertThat(result).isEqualTo(null)
    }

    @Test
    fun newNoteInsertionListener_return2newInsertedItems() = runTest {
        // Given - listen the new insertion listener
        val items = mutableListOf<NoteModel>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.newNoteInsertionListener.collect {
                items.add(it)
            }
        }

        // when - insert 2 items
        val note1Model = note1.toModel()
        val note1Id = repository.insert(note1Model)
        val updateNote1Model = note1Model.copy(
            id = note1Id
        )

        val note2Model = note3.toModel()
        val note2Id = repository.insert(note2Model)
        val updateNote2Model = note1Model.copy(
            id = note2Id
        )

        // Then - expected 2 new inserted items
        Truth.assertThat(items[0]).isEqualTo(note1Model)
        Truth.assertThat(items[1]).isEqualTo(note2Model)
        Truth.assertThat(items.size).isEqualTo(2)

    }

    @Test
    fun updateNoteListener_return2UpdatedItems() = runTest {
        //Given - listen update note listener
        val items = mutableListOf<NoteModel>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.updateNoteListener.collect {
                items.add(it)
            }
        }

        // When - update 2 items
        val note1Model = note1.toModel().copy(
            title = "Title 1 Updated"
        )
        repository.update(note1Model)

        val note2Model = note2.toModel().copy(
            title = "Title 2 updated"
        )
        repository.update(note2Model)

        // Then - expected 2 updated items
        Truth.assertThat(items[0]).isEqualTo(note1Model)
        Truth.assertThat(items[1]).isEqualTo(note2Model)
        Truth.assertThat(items.size).isEqualTo(2)
    }

    @Test
    fun deleteNoteListener_return2DeletedNoteIds() = runTest {
        //Given - listen update note listener
        val ids = mutableListOf<Int>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.deleteNoteListener.collect {
                ids.add(it)
            }
        }

        // When - update 2 items
        val note1Model = note1.toModel()
        repository.delete(note1Model.id)

        val note2Model = note2.toModel()
        repository.delete(note2Model.id)

        // Then - expected 2 updated items
        Truth.assertThat(ids[0]).isEqualTo(note1Model.id)
        Truth.assertThat(ids[1]).isEqualTo(note2Model.id)
        Truth.assertThat(ids.size).isEqualTo(2)
    }



}