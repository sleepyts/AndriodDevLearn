package com.example.myapplicationtest1.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import kotlin.jvm.java


@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {


    class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            println("Sending request: ${request.url} - ${request.headers}")
            val response = chain.proceed(request)
            println("Received response for: ${response.request.url} - ${response.code}")
            return response
        }
    }

    @Provides
    @Named("BaseRetrofit")
    fun provideBaseRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl("http://b000-2001-da8-3001-1020-0-70cb-95d3-3e38.ngrok-free.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideUserApiService(@Named("BaseRetrofit") retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    fun providePlaylistApiService(@Named("BaseRetrofit") retrofit: Retrofit): PlaylistApiService {
        return retrofit.create(PlaylistApiService::class.java)
    }

    @Provides
    fun provideSongApiService(@Named("BaseRetrofit") retrofit: Retrofit): SongApiService {
        return retrofit.create(SongApiService::class.java)
    }

}