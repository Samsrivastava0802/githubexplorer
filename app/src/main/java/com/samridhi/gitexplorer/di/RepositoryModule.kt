package com.samridhi.gitexplorer.di

import com.samridhi.gitexplorer.data.AppApiClientService
import com.samridhi.gitexplorer.data.repositories.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideAppRepository(
        service: AppApiClientService
    ) = AppRepository(service)
}