package com.samridhi.gitexplorer.di

import com.samridhi.gitexplorer.data.repositories.AppRepository
import com.samridhi.gitexplorer.domain.AppUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideAppUseCase(repository: AppRepository) = AppUseCase(repository)
}