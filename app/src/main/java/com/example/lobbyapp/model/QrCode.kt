package com.example.lobbyapp.model

import kotlinx.serialization.Serializable

@Serializable
data class DecodedUserIdRequest(
    val data: String,
)

@Serializable
data class DecodedUserIdResponse(
    val userId: String,
)