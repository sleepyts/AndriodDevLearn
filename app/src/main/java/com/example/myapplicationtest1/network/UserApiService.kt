package com.example.myapplicationtest1.network

import com.example.myapplicationtest1.model.resp.UserDetailResp
import com.example.myapplicationtest1.model.resp.UserPlaylistResp
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {


    /**
     *  Get user detail
     *  @param uid user id
     */
    @GET("/user/detail/")
    suspend fun getUserDetail(@Query("uid") uid: String): UserDetailResp

    /**
     *  Get user playlist
     *  @param uid user id
     */
    @GET("/user/playlist")
    suspend fun getUserPlaylist(@Query("uid") uid: String): UserPlaylistResp
}