package com.example.lobbyapp.util

import android.app.Activity
import android.app.Application
import com.example.lobbyapp.ActivityKey
import com.example.lobbyapp.LobbyAppApplication
import kotlin.system.exitProcess

fun exitApp(activity: Activity, application: Application) {
    activity.finishAndRemoveTask()
    (application as LobbyAppApplication).activityList[ActivityKey.CUSTOMER]!!.finishAndRemoveTask()
    exitProcess(0)
}