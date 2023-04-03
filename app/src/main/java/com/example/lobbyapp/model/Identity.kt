package com.example.lobbyapp.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val result: Boolean,
    val threshold: Float,
    val maxReturns: Int,
    val actualReturns: Int,
    val identities: List<Identity>
)

@Serializable
data class Identity(
    val score: Float = 0F,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val cardNo: String,
    val phoneCode: String,
    val phoneNumber: String,
    val portrait: Portrait,
    val createdAt: String,
    val updatedAt: String,
    val groups: List<String>,
)

@Serializable
data class IdentitySummary(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
)

@Serializable
data class Portrait(
    val data: String = "",
    val pinCode: String,
    val qualityCheck: Boolean,
    val storeFile: Boolean,
    val storeFace: Boolean,
)

@Serializable
data class CreateIdentityRequest(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val cardNo: String,
    val phoneCode: String,
    val phoneNumber: String,
    val portrait: Portrait,
)

@Serializable
data class UpdateIdentityPortraitRequest(
    val portrait: Portrait,
)

@Serializable
data class UpdateIdentityPortraitResponse(
    val portrait: Portrait,
)

@Serializable
data class SearchRequest(
    val image: Base64Image,
    val threshold: Double,
    val maxReturns: Int,
)

@Serializable
data class Base64Image(
    val data: String
)