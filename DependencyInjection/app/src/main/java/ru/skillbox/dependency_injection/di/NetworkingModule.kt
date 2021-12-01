package ru.skillbox.dependency_injection.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import ru.skillbox.dependency_injection.data.Api
import ru.skillbox.dependency_injection.data.AppVersionInterceptor
import ru.skillbox.dependency_injection.data.AppVersionInterceptorImpl
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class VersionInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoggingInterceptor

    @VersionInterceptor
    @Provides
    fun providesAppVersionInterceptor(): Interceptor {
        return AppVersionInterceptorImpl()
    }

    @LoggingInterceptor
    @Provides
    fun providesHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun providesOkHttpClient(
        @VersionInterceptor appVersionInterceptor: Interceptor,
        @LoggingInterceptor httpLoggingInterceptor: Interceptor
    ): OkHttpClient {
        Timber.d("providesOkHttpClient")
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(appVersionInterceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .followRedirects(true)
            .build()
        return okhttpClient
    }

    @Singleton
    @Provides
    fun providesRetrofit(okhttpClient: OkHttpClient): Retrofit {
        Timber.d("providesRetrofit")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://google.com")
            .client(okhttpClient)
            .build()
        return retrofit

    }

    @Singleton
    @Provides
    fun providesApi(retrofit: Retrofit): Api {
        Timber.d("providesApi")
        return retrofit.create()
    }
}