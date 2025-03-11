package com.sundev.testnotes.feature_addNote.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.sundev.testnotes.DummyNotesEntity
import com.sundev.testnotes.core.data.local.FakeNotesDao
import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import com.sundev.testnotes.core.domain.ListenNotesUseCases
import com.sundev.testnotes.core.domain.repository.NotesRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class GetNoteUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NotesRepository
    private lateinit var fakeNotesDao: FakeNotesDao
    private lateinit var useCase: GetNoteUseCase
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
        useCase = GetNoteUseCase(
            repository = repository,
            ioDispatcher = UnconfinedTestDispatcher(testDispatcher.scheduler)
        )
    }

    @Test
    fun getNote_provideValidNoteId_returnNote() = runTest(testDispatcher.scheduler) {
        // Given - setup the useCase

        // When - provide valid note id to get note details
        val noteId = DummyNotesEntity.note3.id ?: -1
        val result = useCase.execute(noteId)

        // Then - expected correct note
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.id).isEqualTo(noteId)

    }

    @Test
    fun getNote_provideInValidNoteId_returnNull() = runTest(testDispatcher.scheduler) {
        // Given - setup the useCase

        // When - provide valid note id to get note details
        val noteId = 99
        val result = useCase.execute(noteId)

        // Then - expected correct note
        Truth.assertThat(result).isNull()
        Truth.assertThat(result?.id).isNotEqualTo(noteId)

    }

}