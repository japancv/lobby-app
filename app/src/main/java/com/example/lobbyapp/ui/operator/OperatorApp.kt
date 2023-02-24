package com.example.lobbyapp.ui.operator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lobbyapp.ActivityKey
import com.example.lobbyapp.LobbyAppApplication
import kotlin.system.exitProcess


enum class OperatorScreen {
    OperatorHome,
    OperatorCannotRecognize,
    OperatorAgreement,
    OperatorUserInfo,
    OperatorConfirmUserInfo,
    OperatorGroupSelection,
    OperatorAccessGranted,
    OperatorSuccessFound,
    OperatorMaintenance,
}

@Composable
fun OperatorApp(
    activity: OperatorActivity,
    navController: NavHostController = rememberNavController(),
    navActions: OperatorNavigation = remember(navController) {
        OperatorNavigation(navController, activity)
    }
) {
    val application = LocalContext.current.applicationContext as LobbyAppApplication

    NavHost(
        navController = navController,
        startDestination = OperatorScreen.OperatorHome.name,
    ) {
        composable(route = OperatorScreen.OperatorHome.name) {
            HomeScreen(
                onCloseButtonClicked = fun() {
                    activity.finishAndRemoveTask()
                    application.activityList[ActivityKey.CUSTOMER]!!.finishAndRemoveTask()
                    exitProcess(0)
                },
                onManageButtonClicked = { navActions.navigateToMaintenance() }
            )
        }
        composable(route = OperatorScreen.OperatorCannotRecognize.name) {
            CannotRecognizeScreen(
                onCancelButtonClicked = { navActions.navigateToHome() },
                onTryAgainButtonClicked = { navActions.navigateToHome() },
                onRegisterButtonClicked = { navActions.navigateToAgreement() }
            )
        }
        composable(route = OperatorScreen.OperatorAgreement.name) {
            AgreementScreen(
                onCancelButtonClicked = { navActions.navigateToHome() },
            )
        }
        composable(route = OperatorScreen.OperatorUserInfo.name) {
            UserInfoScreen(
                onCancelButtonClicked = { navActions.navigateToHome() },
                onContinueButtonClicked = { navActions.navigateToConfirmUserInfo() }
            )
        }
        composable(route = OperatorScreen.OperatorConfirmUserInfo.name) {
            UserInfoConfirmationScreen(
                onCancelButtonClicked = { navActions.navigateToHome() },
                onContinueButtonClicked = { navActions.navigateGroupSelection() },
                onEditButtonClicked = { navActions.navigateToUserInfo() }
            )
        }
        composable(route = OperatorScreen.OperatorGroupSelection.name) {
            GroupSelectionScreen(
                onCancelButtonClicked = { navActions.navigateToHome() },
                onIssueButtonClicked = { navActions.navigateAccessGranted() }
            )
        }
        composable(route = OperatorScreen.OperatorAccessGranted.name) {
            AccessGrantedScreen(
                onGoToHomeButtonClicked = { navActions.navigateToHome() }
            )
        }
        composable(route = OperatorScreen.OperatorSuccessFound.name) {
            SuccessFoundScreen(
                onCancelButtonClicked = { navActions.navigateToHome() },
                onCheckInButtonClicked = { navActions.navigateGroupSelection() },
            )
        }
        composable(route = OperatorScreen.OperatorMaintenance.name) {
            MaintenanceScreen(
                onCancelButtonClicked = { navActions.navigateToHome() },
            )
        }
    }
}
