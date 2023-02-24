package com.example.lobbyapp.network

import com.example.lobbyapp.model.*
import okio.ByteString
import retrofit2.http.*

interface IdpApiService {
    /**
     * Returns a [List] of [IdpGroupSummary]
     */
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("groups")
    suspend fun getGroups(
        @Query("name") name: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): GetIdpGroupsResponse

    /**
     * Add identities to a group
     */
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("groups/{groupId}/identities/add")
    suspend fun addGroupIdentities(
        @Path("groupId") groupId: String,
        @Body request: AddGroupIdentitiesRequest,
    ): IdpGroup

    /**
     * Remove identities from group
     */
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("groups/{groupId}/identities/remove")
    suspend fun removeGroupIdentities(
        @Path("groupId") groupId: String,
        @Body request: RemoveGroupIdentitiesRequest,
    ): IdpGroup

    /**
     * Search for an identity
     */
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("identities/search")
    suspend fun search(
        @Body request: SearchRequest,
    ): SearchResponse

    /**
     * Create an identity
     */
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("identities")
    suspend fun createIdentity(
        @Body request: CreateIdentityRequest,
    ): Identity

    /**
     * Get identities in a group
     */
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("groups/{groupId}")
    suspend fun getGroup(
        @Path("groupId") groupId: String,
    ): IdpGroup

    /**
     * Get Identity Image
     */
    @Headers(
        "Accept: image/jpeg"
    )
    @GET("identities/{identityId}/portrait")
    suspend fun getPortrait(
        @Path("identityId") identityId: String,
    ): ByteString

    /**
     * Get Identity
     */
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("identities/{identityId}")
    suspend fun getIdentity(
        @Path("identityId") identityId: String,
    ): Identity
}
