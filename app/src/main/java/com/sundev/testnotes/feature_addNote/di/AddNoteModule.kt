package com.sundev.testnotes.feature_addNote.di

import com.sundev.testnotes.core.domain.repository.NotesRepository
import com.sundev.testnotes.feature_addNote.domain.AddNoteUseCase
import com.sundev.testnotes.feature_addNote.domain.DeleteNoteUseCase
import com.sundev.testnotes.feature_addNote.domain.GetNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object AddNoteModule {

    @Provides
    fun provideGetNoteUseCase(repository: NotesRepository) : GetNoteUseCase{
        return GetNoteUseCase(repository)
    }

    @Provides
    fun provideAddNoteUseCases(repository: NotesRepository) : AddNoteUseCase {
        return AddNoteUseCase(repository)
    }

    @Provides
    fun provideDeleteNoteUseCases(repository: NotesRepository) : DeleteNoteUseCase {
        return DeleteNoteUseCase(repository)
    }

}