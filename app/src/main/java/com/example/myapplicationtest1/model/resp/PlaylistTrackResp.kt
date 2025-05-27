package com.example.myapplicationtest1.model.resp

data class PlaylistTrackResp(
    val songs: List<Song>
)

data class Song(
    val name: String,

    val mainTitle: String,

    // Artists
    val ar: List<Ar>,
    // Albums
    val al: Al,

    val additionalTitle: String,

    val id: String
)

data class Ar(
    val id: String,
    val name: String
)

data class Al(
    val id: String,
    val name: String,
    val picUrl: String
)