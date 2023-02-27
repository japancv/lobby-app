package com.example.lobbyapp

import android.app.Activity
import android.app.Application
import com.example.lobbyapp.data.AppContainer
import com.example.lobbyapp.data.DefaultAppContainer
import java.io.FileNotFoundException

enum class ActivityKey {
    OPERATOR,
    CUSTOMER
}

class LobbyAppApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    var activityList = mapOf<ActivityKey, Activity>()

    fun initContainer() {
        try {
            container = DefaultAppContainer()
        } catch (e: FileNotFoundException) {
        }
    }

    fun addActivity(key: ActivityKey, activity: Activity) {
        activityList += Pair(key, activity)
    }
}
