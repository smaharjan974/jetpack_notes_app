package com.sundev.testnotes.feature_home.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.sundev.testnotes.DummyNotesEntity
import com.sundev.testnotes.Routes
import com.sundev.testnotes.core.data.local.FakeNotesDao
import com.sundev.testnotes.core.data.local.toModel
import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import com.sundev.testnotes.core.domain.ListenNotesUseCases
import com.sundev.testnotes.core.domain.repository.NotesRepository
import com.sundev.testnotes.feature_home.domain.GetNotesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
): TestWatcher(){
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instanceTaskExecutor = InstantTaskExecutorRule()

    private lateinit var _getNoteUseCase: GetNotesUseCase
    private lateinit var _listenNoteUseCase: ListenNotesUseCases
    private lateinit var _repository: NotesRepository
    private lateinit var _fakeNotesDao: FakeNotesDao
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setupViewModel() {
        val items =
            arrayListOf(DummyNotesEntity.note1, DummyNotesEntity.note2, DummyNotesEntity.note3)
        _fakeNotesDao = FakeNotesDao(items = items)
        _repository = NotesRepositoryImpl(
            dao = _fakeNotesDao
        )
        _getNoteUseCase = GetNotesUseCase(
            repository = _repository,
            ioDispatcher = UnconfinedTestDispatcher(testDispatcher.scheduler)
        )
        _listenNoteUseCase = ListenNotesUseCases(
            repository = _repository,
            UnconfinedTestDispatcher(testDispatcher.scheduler)
        )
        viewModel = HomeViewModel(
            getNotesUseCase = _getNoteUseCase,
            listenNotesUseCases = _listenNoteUseCase,
            ioDispatcher = UnconfinedTestDispatcher(testDispatcher.scheduler),
            mainDispatcher = UnconfinedTestDispatcher(testDispatcher.scheduler),
        )
    }

    @Test
    fun getNotes_return3Notes() = runTest(testDispatcher.scheduler) {
        // Given - setup the viewModel

        // When - Get the notes list
        advanceUntilIdle()
        val result = viewModel.noteList

        // Then - expected 3 items
        Truth.assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun listenNotes_insertNewNote_returnInsertNote() = runTest(testDispatcher.scheduler) {
        // Give - setup the viewModel &


        // When - insert a new note
        advanceUntilIdle()
        var newNote = DummyNotesEntity.note4.toModel()
        val newNoteId = _repository.insert(newNote)
        newNote = newNote.copy(
            id = newNoteId
        )
        val result = viewModel.noteList.firstOrNull()

        //Then - expected Insert New Note
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isEqualTo(newNote)
    }

    @Test
    fun listenNotes_updateNewNote_returnUpdatedNote() = runTest(testDispatcher.scheduler) {

        // Give - setup the viewModel &


        // When - insert a new note
        advanceUntilIdle()
        var updatedNote = DummyNotesEntity.note2.toModel().copy(
            title = "Test"
        )
        _repository.update(updatedNote)
        val result = viewModel.noteList.firstOrNull { it.id == updatedNote.id }

        //Then - expected Insert New Note
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result).isEqualTo(updatedNote)
    }

    @Test
    fun listenNotes_deleteExistingNote_returnNull() = runTest(testDispatcher.scheduler) {

        // Give - setup the viewModel &


        // When - insert a new note
        advanceUntilIdle()
        var updatedNote = DummyNotesEntity.note1.toModel().id
        _repository.delete(updatedNote)
        val result = viewModel.noteList.firstOrNull { it.id == updatedNote }

        //Then - expected Insert New Note
        Truth.assertThat(result).isNull()
    }

    @Test
    fun listenNotes_deleteInValidNote_returnNull() = runTest(testDispatcher.scheduler) {

        // Give - setup the viewModel &


        // When - insert a new note
        advanceUntilIdle()
        var updatedNote = DummyNotesEntity.note5.toModel().id
        _repository.delete(updatedNote)
        val result = viewModel.noteList.firstOrNull { it.id == updatedNote }

        //Then - expected Insert New Note
        Truth.assertThat(result).isNull()
    }

    @Test
    fun addNewNote_returnNavigateNextEvent() = runTest(testDispatcher.scheduler) {
        // Given - setup the viewModel
        val events = mutableListOf<HomeEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.collect { newEvent ->
                events.add(newEvent)
            }
        }

        // When - call addNewNoteMethod
        val method = viewModel.javaClass.getDeclaredMethod("addNewNote")
        method.isAccessible = true
        method.invoke(viewModel)
        val result = events.lastOrNull()?.let { it as HomeEvent.NavigateNext }

        // Then - expecting navigate next event
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.javaClass?.simpleName)
            .isEqualTo(HomeEvent.NavigateNext::class.java.simpleName)
        Truth.assertThat(result?.route).isEqualTo(Routes.ADD_NOTE + "/-1")
    }

    @Test
    fun listenItemOnClick_returnNavigateNextEvent() = runTest(testDispatcher.scheduler) {
        // Given - setup the viewModel
        val events = mutableListOf<HomeEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.eventFlow.collect { newEvent ->
                events.add(newEvent)
            }
        }

        // When - call addNewNoteMethod
        val noteId = DummyNotesEntity.note2.toModel().id
        val params = arrayOfNulls<Any>(1)
        val method = viewModel.javaClass.getDeclaredMethod("listItemOnClick",Int::class.java)
        params[0] = noteId
        method.isAccessible = true
        method.invoke(viewModel, *params)
        val result = events.lastOrNull()?.let { it as HomeEvent.NavigateNext }

        // Then - expecting navigate next event
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.javaClass?.simpleName)
            .isEqualTo(HomeEvent.NavigateNext::class.java.simpleName)
        Truth.assertThat(result?.route).isEqualTo(Routes.ADD_NOTE + "/$noteId")
    }


}