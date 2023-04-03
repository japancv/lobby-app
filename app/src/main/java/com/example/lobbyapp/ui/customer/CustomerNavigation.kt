package com.example.lobbyapp.ui.customer

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.example.lobbyapp.ui.operator.OperatorActivity
import com.example.lobbyapp.ui.operator.OperatorScreen
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel

inline fun CustomerNavigation.startOperatorActivity(block: Intent.() -> Unit) {
    Log.d("nav-customer", "startOperatorActivity: $activity.localClassName")
    activity.startActivity(Intent(activity, OperatorActivity::class.java).apply {
        block()
    })
}

fun NavOptionsBuilder.popUpAll(navController: NavHostController) {
    launchSingleTop = true
    navController.popBackStack(navController.graph.findStartDestination().id, false)
}

class CustomerNavigation(
    private val navController: NavHostController,
    val activity: CustomerActivity,
) {
    private var _currentDestination = mutableStateOf(CustomerScreen.FaceRecognition.name)
    var currentDestination = _currentDestination

    private fun popUpAll(destination: String) {
        Log.d("nav-customer", "popUpAll: $destination")

        if (_currentDestination.value !== destination) {
            navController.navigate(destination) { popUpAll(navController) }
            _currentDestination.value = destination
        }
    }

    fun navigateToFaceRecognition(shouldResetForm: Boolean = true) {
        startOperatorActivity {
            action = "${activity.packageName}.${OperatorScreen.OperatorHome.name}"
        }
        popUpAll(CustomerScreen.FaceRecognition.name)
        if (shouldResetForm) {
            UserInfoViewModel.resetForm()
        }
    }

    fun navigateToCannotRecognize() {
        startOperatorActivity {
            action = "${activity.packageName}.${OperatorScreen.OperatorCannotRecognize.name}"
        }
        popUpAll(CustomerScreen.CannotRecognize.name)
    }

    fun navigateToAgreement() {
        startOperatorActivity {
            action = "${activity.packageName}.${OperatorScreen.OperatorAgreement.name}"
        }
        popUpAll(CustomerScreen.Agreement.name)
    }

    fun navigateToUserInfo() {
        startOperatorActivity {
            action = "${activity.packageName}.${OperatorScreen.OperatorUserInfo.name}"
        }
        popUpAll(CustomerScreen.UserInfo.name)
    }

    fun navigateToWaitForCheckInConfirmation() {
        startOperatorActivity {
            action = "${activity.packageName}.${OperatorScreen.OperatorSuccessFound.name}"
        }
        popUpAll(CustomerScreen.WaitForConfirmation.name)
    }

    fun navigateToWaitForConfirmation() {
        startOperatorActivity {
            action = "${activity.packageName}.${OperatorScreen.OperatorConfirmUserInfo.name}"
        }
        popUpAll(CustomerScreen.WaitForConfirmation.name)
    }

    fun navigateToAccessGranted() {
        popUpAll(CustomerScreen.AccessGranted.name)
    }

    fun navigateToMaintenance() {
        popUpAll(CustomerScreen.MaintenanceScreen.name)
    }

    fun navigateToWelcome() {
        startOperatorActivity {
            action = "${activity.packageName}.${OperatorScreen.OperatorWelcome.name}"
        }
        popUpAll(CustomerScreen.WelcomeScreen.name)
    }
}
