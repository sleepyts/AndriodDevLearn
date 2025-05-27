package com.example.myapplicationtest1.model.resp

data class UserPlaylistResp(
    val playlist: List<Playlist>
)

data class Playlist(
    val coverImgUrl: String,
    val name: String,
    val playCount: Int,
    val trackCount: Int,
    val id: String

)
