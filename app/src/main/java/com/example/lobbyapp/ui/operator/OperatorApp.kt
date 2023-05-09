package com.example.lobbyapp.ui.operator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lobbyapp.LobbyAppApplication
import com.example.lobbyapp.ui.viewModel.QrCodeViewModel
import com.example.lobbyapp.ui.viewModel.StandbyViewModel
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import com.example.lobbyapp.util.exitApp


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
    OperatorWelcome,
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
    val qrCodeViewModel: QrCodeViewModel =
        viewModel(factory = QrCodeViewModel.Factory)
    val standbyViewModel: StandbyViewModel =
        viewModel(factory = StandbyViewModel.Factory)

    fun navigateToHome(shouldResetForm: Boolean = true, enableQrCode: Boolean = false) {
        if (shouldResetForm) {
            UserInfoViewModel.resetForm()
        }
        if (shouldResetForm && !enableQrCode) {
            standbyViewModel.reset()
        }

        qrCodeViewModel.setEnabled(enableQrCode)
        navActions.navigateToHome()
    }

    NavHost(
        navController = navController,
        startDestination = OperatorScreen.OperatorHome.name,
    ) {
        composable(route = OperatorScreen.OperatorHome.name) {
            HomeScreen(
                onCloseButtonClicked = {
                    exitApp(
                        activity = activity,
                        application = application
                    )
                },
                onManageButtonClicked = { navActions.navigateToMaintenance() }
            )
        }
        composable(route = OperatorScreen.OperatorCannotRecognize.name) {
            CannotRecognizeScreen(
                onCancelButtonClicked = { navigateToHome() },
                onHomeButtonClicked = { navigateToHome() },
                onRegisterButtonClicked = { navActions.navigateToAgreement() },
                onScanButtonClicked = { navigateToHome(enableQrCode = true) }
            )
        }
        composable(route = OperatorScreen.OperatorAgreement.name) {
            AgreementScreen(
                onCancelButtonClicked = { navigateToHome() },
            )
        }
        composable(route = OperatorScreen.OperatorUserInfo.name) {
            UserInfoScreen(
                onCancelButtonClicked = { navigateToHome() },
                onContinueButtonClicked = { navActions.navigateToConfirmUserInfo() }
            )
        }
        composable(route = OperatorScreen.OperatorConfirmUserInfo.name) {
            UserInfoConfirmationScreen(
                onCancelButtonClicked = { navigateToHome() },
                onContinueButtonClicked = { navActions.navigateGroupSelection() },
                onEditButtonClicked = { navActions.navigateToUserInfo() },
                onRetryButtonClicked = { navigateToHome(shouldResetForm = false) }
            )
        }
        composable(route = OperatorScreen.OperatorGroupSelection.name) {
            GroupSelectionScreen(
                onCancelButtonClicked = { navigateToHome() },
                onIssueButtonClicked = { navActions.navigateAccessGranted() }
            )
        }
        composable(route = OperatorScreen.OperatorAccessGranted.name) {
            AccessGrantedScreen(
                onGoToHomeButtonClicked = { navigateToHome() }
            )
        }
        composable(route = OperatorScreen.OperatorSuccessFound.name) {
            SuccessFoundScreen(
                onCancelButtonClicked = { navigateToHome() },
                onCheckInButtonClicked = { navActions.navigateGroupSelection() },
            )
        }
        composable(route = OperatorScreen.OperatorMaintenance.name) {
            MaintenanceScreen(
                onCancelButtonClicked = { navigateToHome() },
            )
        }
        composable(route = OperatorScreen.OperatorWelcome.name) {
            WelcomeScreen(
                onGoToNextScreen = { navActions.navigateToAgreement() },
                onCancelButtonClicked = { navigateToHome() },
            )
        }
    }
}
