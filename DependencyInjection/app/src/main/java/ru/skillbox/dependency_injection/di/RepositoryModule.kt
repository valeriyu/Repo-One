package ru.skillbox.dependency_injection.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.skillbox.dependency_injection.data.Api
import ru.skillbox.dependency_injection.data.ImagesRepository
import ru.skillbox.dependency_injection.data.ImagesRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    fun providesMovieRepository(@ApplicationContext context: Context, api: Api): ImagesRepository {
        return ImagesRepositoryImpl(context = context, api = api)
    }
}
