package com.example.myapplicationtest1.model.resp

data class UserDetailResp(
    val profile: Profile

)

data class Profile(
    val avatarUrl: String,
    val nickname: String
)
