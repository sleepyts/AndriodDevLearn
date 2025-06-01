package com.example.myapplicationtest1.model.resp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PlaylistTrackResp(
    val songs: List<Song>
)


@Parcelize
data class Song(
    val name: String = "",

    val mainTitle: String = "",

    // Artists
    val ar: List<Ar> = emptyList(),
    // Albums
    val al: Al = Al(),

    val additionalTitle: String = "",

    val id: String = ""
) : Parcelable

@Parcelize
data class Ar(
    val id: String,
    val name: String
) : Parcelable

@Parcelize
data class Al(
    val id: String = "",
    val name: String = "",
    val picUrl: String = ""
) : Parcelable