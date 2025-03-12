package com.sundev.testnotes.feature_home.di

import com.sundev.testnotes.core.di.IoDispatcher
import com.sundev.testnotes.core.di.MainDispatcher
import com.sundev.testnotes.core.domain.repository.NotesRepository
import com.sundev.testnotes.feature_home.domain.GetNotesUseCase
import com.sundev.testnotes.core.domain.ListenNotesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

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

    @IoDispatcher
    @Provides
    fun provideIODispatcher() : CoroutineDispatcher {
        return Dispatchers.IO
    }

    @MainDispatcher
    @Provides
    fun provideMainDispatcher() : CoroutineDispatcher {
        return Dispatchers.Main
    }
}