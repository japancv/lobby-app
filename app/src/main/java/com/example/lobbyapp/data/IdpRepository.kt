package com.example.lobbyapp.data

import com.example.lobbyapp.model.AddGroupIdentitiesRequest
import com.example.lobbyapp.model.DecodedUserIdRequest
import com.example.lobbyapp.model.DecodedUserIdResponse
import com.example.lobbyapp.model.GetIdpGroupsResponse
import com.example.lobbyapp.model.Identity
import com.example.lobbyapp.model.IdpGroup
import com.example.lobbyapp.model.RemoveGroupIdentitiesRequest
import com.example.lobbyapp.network.IdpApiService
import okio.ByteString
import java.util.Properties

/**
 * Repository that fetch IDP groups list.
 */
interface IdpRepository {
    suspend fun getGroups(): GetIdpGroupsResponse
    suspend fun getGroup(groupId: String): IdpGroup
    suspend fun getIdentity(identityId: String): Identity
    suspend fun getPortrait(identityId: String): ByteString
    suspend fun addGroupIdentities(
        groupId: String,
        request: AddGroupIdentitiesRequest
    ): IdpGroup

    suspend fun removeGroupIdentities(
        groupId: String,
        request: RemoveGroupIdentitiesRequest
    ): IdpGroup

    suspend fun getDecodedUserId(
        request: DecodedUserIdRequest
    ): DecodedUserIdResponse
}

class NetworkIdpRepository(
    private val idpApiService: IdpApiService,
    private val configProperties: Properties
) : IdpRepository {
    override suspend fun getGroups(): GetIdpGroupsResponse {
        return idpApiService.getGroups(name = configProperties.getProperty("PREFIX"))
    }

    override suspend fun getGroup(
        groupId: String,
    ): IdpGroup {
        return idpApiService.getGroup(groupId = groupId)
    }

    override suspend fun addGroupIdentities(
        groupId: String,
        request: AddGroupIdentitiesRequest
    ): IdpGroup {
        return idpApiService.addGroupIdentities(groupId = groupId, request = request)
    }

    override suspend fun removeGroupIdentities(
        groupId: String,
        request: RemoveGroupIdentitiesRequest
    ): IdpGroup {
        return idpApiService.removeGroupIdentities(groupId = groupId, request = request)
    }

    override suspend fun getIdentity(
        identityId: String,
    ): Identity {
        return idpApiService.getIdentity(identityId = identityId)
    }

    override suspend fun getPortrait(
        identityId: String,
    ): ByteString {
        return idpApiService.getPortrait(identityId = identityId)
    }

    override suspend fun getDecodedUserId(request: DecodedUserIdRequest): DecodedUserIdResponse {
        return idpApiService.getDecodedUserId(request = request)
    }
}
