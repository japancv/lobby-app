package com.example.lobbyapp.data

import com.example.lobbyapp.network.IdpApiService
import com.example.lobbyapp.util.readConfigFromFile
import java.util.*

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val idpRepository: IdpRepository
    val configProperties: Properties
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    override val configProperties = readConfigFromFile()
    private val apiClient = ApiClient.getInstance()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService by lazy {
        apiClient.create(IdpApiService::class.java)
    }

    /**
     * The implementation for IDP repository
     */
    override val idpRepository by lazy {
        NetworkIdpRepository(retrofitService, this.configProperties)
    }
}