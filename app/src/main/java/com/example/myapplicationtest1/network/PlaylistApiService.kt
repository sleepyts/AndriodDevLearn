package com.example.myapplicationtest1.network

import com.example.myapplicationtest1.model.resp.PlaylistTrackResp
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaylistApiService {

    /**
     * Get all tracks of a playlist
     * @param id playlist id
     * @param limit result limit
     * @param offset result offset
     */
    @GET("/playlist/track/all")
    suspend fun getPlaylistTracks(
        @Query("id") id: String,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int = 0
    ): PlaylistTrackResp
}