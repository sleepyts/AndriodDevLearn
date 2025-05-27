package com.example.myapplicationtest1.network

import com.example.myapplicationtest1.model.resp.SongUrlResp
import retrofit2.http.GET
import retrofit2.http.Query

interface SongApiService {

    @GET("/song/url/v1")
    suspend fun getSongUrl(@Query("id") id: String, @Query("level") level: String): SongUrlResp

}

