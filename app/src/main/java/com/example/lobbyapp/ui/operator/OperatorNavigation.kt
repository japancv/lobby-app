package com.example.lobbyapp.ui.operator

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import com.example.lobbyapp.ui.customer.CustomerActivity
import com.example.lobbyapp.ui.customer.CustomerScreen
import com.example.lobbyapp.ui.customer.popUpAll
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel

inline fun OperatorNavigation.startCustomerActivity(block: Intent.() -> Unit) {
    Log.d("nav-operator", "startCustomerActivity: $activity.localClassName")
    activity.startActivity(Intent(activity, CustomerActivity::class.java).apply {
        block()
    })
}

class OperatorNavigation(
    private val navController: NavHostController,
    val activity: OperatorActivity
) {
    private var _currentDestination = mutableStateOf(OperatorScreen.OperatorHome.name)
    var currentDestination = _currentDestination

    private fun popUpAll(destination: String) {
        Log.d("nav-operator", "popUpAll: $destination")

        if (_currentDestination.value !== destination) {
            navController.navigate(destination) { popUpAll(navController) }
            _currentDestination.value = destination
        }
    }

    fun navigateToHome(ifSelfTriggered: Boolean = false) {
        if (!ifSelfTriggered) {
            startCustomerActivity {
                action = "${activity.packageName}.${CustomerScreen.FaceRecognition.name}"
            }
        }
        popUpAll(OperatorScreen.OperatorHome.name)
        UserInfoViewModel.resetForm()
    }

    fun navigateToCannotRecognize(ifSelfTriggered: Boolean = false) {
        if (!ifSelfTriggered) {
            startCustomerActivity {
                action = "${activity.packageName}.${CustomerScreen.CannotRecognize.name}"
            }
        }
        popUpAll(OperatorScreen.OperatorCannotRecognize.name)
    }

    fun navigateToAgreement(ifSelfTriggered: Boolean = false) {
        if (!ifSelfTriggered) {
            startCustomerActivity {
                Log.d(
                    "nav-operator",
                    "actionName: ${activity.packageName}.${CustomerScreen.Agreement.name}"
                )
                action = "${activity.packageName}.${CustomerScreen.Agreement.name}"
            }
        }
        popUpAll(OperatorScreen.OperatorAgreement.name)
    }

    fun navigateToUserInfo(ifSelfTriggered: Boolean = false) {
        if (!ifSelfTriggered) {
            startCustomerActivity {
                Log.d(
                    "nav-operator",
                    "actionName: ${activity.packageName}.${CustomerScreen.UserInfo.name}"
                )
                action = "${activity.packageName}.${CustomerScreen.UserInfo.name}"
            }
        }
        popUpAll(OperatorScreen.OperatorUserInfo.name)
    }

    fun navigateToConfirmUserInfo(ifSelfTriggered: Boolean = false) {
        if (!ifSelfTriggered) {
            startCustomerActivity {
                action = "${activity.packageName}.${CustomerScreen.WaitForConfirmation.name}"
            }
        }
        popUpAll(OperatorScreen.OperatorConfirmUserInfo.name)
    }

    fun navigateGroupSelection() {
        popUpAll(OperatorScreen.OperatorGroupSelection.name)
    }

    fun navigateToMaintenance(ifSelfTriggered: Boolean = false) {
        if (!ifSelfTriggered) {
            startCustomerActivity {
                action = "${activity.packageName}.${CustomerScreen.MaintenanceScreen.name}"
            }
        }
        popUpAll(OperatorScreen.OperatorMaintenance.name)
    }

    fun navigateAccessGranted(ifSelfTriggered: Boolean = false) {
        if (!ifSelfTriggered) {
            startCustomerActivity {
                action = "${activity.packageName}.${CustomerScreen.AccessGranted.name}"
            }
        }
        popUpAll(OperatorScreen.OperatorAccessGranted.name)
    }

    fun navigateSuccessFound(ifSelfTriggered: Boolean = false) {
        popUpAll(OperatorScreen.OperatorSuccessFound.name)
    }
}