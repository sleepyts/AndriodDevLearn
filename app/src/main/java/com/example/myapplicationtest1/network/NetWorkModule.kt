package com.example.myapplicationtest1.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import kotlin.jvm.java


@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Provides
    @Named("BaseRetrofit")
    fun provideBaseRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://b28f-2001-da8-3001-1020-2-b0cb-95d3-3e38.ngrok-free.app")
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

}