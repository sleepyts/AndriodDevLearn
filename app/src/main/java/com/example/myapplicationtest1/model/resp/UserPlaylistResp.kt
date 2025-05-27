package com.example.myapplicationtest1.model.resp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class UserPlaylistResp(
    val playlist: List<Playlist>
)

@Parcelize
data class Playlist(
    val coverImgUrl: String,
    val name: String,
    val playCount: Int,
    val trackCount: Int,
    val id: String

) : Parcelable
