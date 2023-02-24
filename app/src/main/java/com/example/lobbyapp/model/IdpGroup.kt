package com.example.lobbyapp.model;

import kotlinx.serialization.Serializable

@Serializable
data class GetIdpGroupsResponse(
    val total: Int,
    val count: Int,
    val limit: Int,
    val offset: Int,
    val groups: List<IdpGroupSummary>
)

@Serializable
data class IdpGroupSummary(
    val id: String,
    val name: String,
    val count: Int,
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
data class AddGroupIdentitiesRequest(
    val userIds: List<String>
)

@Serializable
data class RemoveGroupIdentitiesRequest(
    val userIds: List<String>
)

@Serializable
data class IdpGroup(
    val id: String,
    val name: String,
    val identities: List<IdentitySummary>,
    val createdAt: String,
    val updatedAt: String,
)