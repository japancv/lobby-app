package com.example.lobbyapp.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerErrorResponse(
    val code: String,
    val name: String,
    val message: String,
)