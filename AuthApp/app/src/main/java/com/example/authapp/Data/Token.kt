package com.example.authapp.Data

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("success")
    val success: String,
    @SerializedName("response")
    val response: TokenResponse,
)

data class TokenResponse(
    @SerializedName("token")
    val token: String
)