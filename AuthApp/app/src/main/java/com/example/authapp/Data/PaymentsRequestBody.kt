package com.example.authapp.Data

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class PaymentsRequestBody(
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String
)
