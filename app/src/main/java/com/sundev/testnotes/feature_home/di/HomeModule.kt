package com.sundev.testnotes.feature_home.di

import android.content.Context
import androidx.room.Room
import com.sundev.testnotes.NotesApp
import com.sundev.testnotes.core.data.local.AppDatabase
import com.sundev.testnotes.core.data.repository.NotesRepositoryImpl
import com.sundev.testnotes.core.domain.repository.NotesRepository
import com.sundev.testnotes.feature_home.domain.GetNotesUseCase
import com.sundev.testnotes.feature_home.domain.ListenNotesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {
    @Provides
    fun provideGetNotesUseCase(repository: NotesRepository) : GetNotesUseCase{
        return GetNotesUseCase(repository)
    }

    @Provides
    fun provideListenNotesUseCase(repository: NotesRepository) : ListenNotesUseCases {
        return ListenNotesUseCases(repository)
    }
}