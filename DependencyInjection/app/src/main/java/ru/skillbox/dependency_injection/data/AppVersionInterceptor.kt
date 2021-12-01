package ru.skillbox.dependency_injection.data

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

interface AppVersionInterceptor {
    fun intercept(chain: Interceptor.Chain): Response
}
