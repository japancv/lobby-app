package com.example.lobbyapp.ui.customer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.lobbyapp.ActivityKey
import com.example.lobbyapp.LobbyAppApplication
import com.example.lobbyapp.ui.component.ErrorScreen
import com.example.lobbyapp.ui.component.ErrorType
import com.example.lobbyapp.ui.theme.Colors
import com.example.lobbyapp.ui.theme.LobbyAppTheme

class CustomerActivity : ComponentActivity() {
    private var navActions: CustomerNavigation? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as LobbyAppApplication).addActivity(ActivityKey.CUSTOMER, this)
        hideNavigationBar()

        if (!intent.getBooleanExtra("isConfigExisted", true)) {
            setContent {
                LobbyAppTheme {
                    ErrorScreen(errorType = ErrorType.CONFIG_NOT_FOUND)
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
                            errorType = ErrorType.INTERNET_NOT_AVAILABLE
                        )
                        return@LobbyAppTheme
                    } else if (intent.getStringExtra("missingConfigFields")?.isNotEmpty() == true) {
                        ErrorScreen(
                            errorType = ErrorType.CONFIG_REQUIRED_FIELD_MISSING,
                            extraMessage = intent.getStringExtra("missingConfigFields")!!
                        )
                        return@LobbyAppTheme
                    } else if (!intent.getBooleanExtra("isLogoFilePathLegal", true)) {
                        ErrorScreen(
                            errorType = ErrorType.ILLEGAL_LOGO_PATH
                        )
                        return@LobbyAppTheme
                    }

                    val navController = rememberNavController()
                    navActions = CustomerNavigation(navController, this@CustomerActivity).also {
                        CustomerApp(
                            activity = this@CustomerActivity,
                            navController = navController,
                            navActions = remember { it },
                        )
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onResume() {
        super.onResume()

        hideNavigationBar()
    }

    override fun onStop() {
        super.onStop()

        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            intent.action?.let { Log.d("nav-customer", "onNewIntent: $it") }
        }
        intent?.let {
            when (it.action ?: "$packageName.${CustomerScreen.FaceRecognition.name}") {
                "$packageName.${CustomerScreen.CannotRecognize.name}" -> {
                    navActions?.navigateToCannotRecognize()
                }
                "$packageName.${CustomerScreen.Agreement.name}" -> {
                    navActions?.navigateToAgreement()
                }
                "$packageName.${CustomerScreen.UserInfo.name}" -> {
                    navActions?.navigateToUserInfo()
                }
                "$packageName.${CustomerScreen.WaitForConfirmation.name}" -> {
                    navActions?.navigateToWaitForConfirmation()
                }
                "$packageName.${CustomerScreen.AccessGranted.name}" -> {
                    navActions?.navigateToAccessGranted()
                }
                "$packageName.${CustomerScreen.MaintenanceScreen.name}" -> {
                    navActions?.navigateToMaintenance()
                }
                "$packageName.${CustomerScreen.WelcomeScreen.name}" -> {
                    navActions?.navigateToWelcome()
                }
                else -> {
                    navActions?.navigateToFaceRecognition()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Suppress("DEPRECATION")
    @SuppressLint("WrongConstant")
    private fun hideNavigationBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}