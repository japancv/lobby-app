package com.example.lobbyapp.ui.operator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.lobbyapp.ActivityKey
import com.example.lobbyapp.LobbyAppApplication
import com.example.lobbyapp.ui.component.ErrorScreen
import com.example.lobbyapp.ui.component.ErrorType
import com.example.lobbyapp.ui.theme.Colors
import com.example.lobbyapp.ui.theme.LobbyAppTheme
import com.example.lobbyapp.util.exitApp

class OperatorActivity : ComponentActivity() {
    private var navActions: OperatorNavigation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as LobbyAppApplication).addActivity(ActivityKey.OPERATOR, this)

        if (!intent.getBooleanExtra("isConfigExisted", true)) {
            setContent {
                LobbyAppTheme {
                    ErrorScreen(isSecondaryDisplay = true, errorType = ErrorType.CONFIG_NOT_FOUND)
                }
            }
        } else {
            val configProperties = (application as LobbyAppApplication).container.configProperties

            setContent {
                LobbyAppTheme(
                    colors = Colors(
                        primaryColor = configProperties.getProperty("PRIMARY_COLOR"),
                        secondaryColor = configProperties.getProperty("SECONDARY_COLOR"),
                        backgroundColor = configProperties.getProperty("BACKGROUND_COLOR"),
                        textColor = configProperties.getProperty("TEXT_COLOR")
                    )
                ) {
                    if (!intent.getBooleanExtra("isInternetAvailable", true)) {
                        ErrorScreen(
                            isSecondaryDisplay = true,
                            errorType = ErrorType.INTERNET_NOT_AVAILABLE,
                            showCloseButton = true,
                            onCloseButtonClicked = {
                                exitApp(
                                    activity = this,
                                    application = application
                                )
                            }
                        )
                        return@LobbyAppTheme
                    } else if (intent.getStringExtra("missingConfigFields")?.isNotEmpty() == true) {
                        ErrorScreen(
                            isSecondaryDisplay = true,
                            errorType = ErrorType.CONFIG_REQUIRED_FIELD_MISSING,
                            extraMessage = intent.getStringExtra("missingConfigFields")!!,
                            showCloseButton = true,
                            onCloseButtonClicked = {
                                exitApp(
                                    activity = this,
                                    application = application
                                )
                            }
                        )
                        return@LobbyAppTheme
                    } else if (!intent.getBooleanExtra("isLogoFilePathLegal", true)) {
                        ErrorScreen(
                            isSecondaryDisplay = true,
                            errorType = ErrorType.ILLEGAL_LOGO_PATH,
                            showCloseButton = true,
                            onCloseButtonClicked = {
                                exitApp(
                                    activity = this,
                                    application = application
                                )
                            }
                        )
                        return@LobbyAppTheme
                    }

                    val navController = rememberNavController()
                    navActions = OperatorNavigation(navController, this@OperatorActivity).also {
                        OperatorApp(
                            activity = this@OperatorActivity,
                            navController = navController,
                            navActions = remember { it },
                        )
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()

        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            intent.action?.let { Log.d("nav-operator", "onNewIntent: $it") }
        }
        intent?.let {
            when (it.action ?: "$packageName.${OperatorScreen.OperatorHome.name}") {
                "$packageName.${OperatorScreen.OperatorCannotRecognize.name}" -> {
                    navActions?.navigateToCannotRecognize(true)
                }
                "$packageName.${OperatorScreen.OperatorAgreement.name}" -> {
                    navActions?.navigateToAgreement(true)
                }
                "$packageName.${OperatorScreen.OperatorUserInfo.name}" -> {
                    navActions?.navigateToUserInfo(true)
                }
                "$packageName.${OperatorScreen.OperatorConfirmUserInfo.name}" -> {
                    navActions?.navigateToConfirmUserInfo(true)
                }
                "$packageName.${OperatorScreen.OperatorGroupSelection.name}" -> {
                    navActions?.navigateGroupSelection()
                }
                "$packageName.${OperatorScreen.OperatorAccessGranted.name}" -> {
                    navActions?.navigateAccessGranted(true)
                }
                "$packageName.${OperatorScreen.OperatorSuccessFound.name}" -> {
                    navActions?.navigateSuccessFound(true)
                }
                "$packageName.${OperatorScreen.OperatorWelcome.name}" -> {
                    navActions?.navigateWelcome()
                }
                else -> {
                    navActions?.navigateToHome(true)
                }
            }
        }
    }
}