package com.example.lobbyapp.ui.customer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lobbyapp.ui.viewModel.QrCodeViewModel
import com.example.lobbyapp.ui.viewModel.UserInfoViewModel
import java.util.*

enum class CustomerScreen {
    Agreement,
    FaceRecognition,
    CannotRecognize,
    UserInfo,
    WaitForConfirmation,
    AccessGranted,
    MaintenanceScreen,
    WelcomeScreen,
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CustomerApp(
    activity: CustomerActivity,
    navController: NavHostController = rememberNavController(),
    navActions: CustomerNavigation = remember(navController) {
        CustomerNavigation(navController, activity)
    }
) {
    val qrCodeViewModel: QrCodeViewModel =
        viewModel(factory = QrCodeViewModel.Factory)

    fun navigateToFaceRecognition(shouldResetForm: Boolean = true, enableQrCode: Boolean = false) {
        if (shouldResetForm) {
            UserInfoViewModel.resetForm()
        }

        qrCodeViewModel.setEnabled(enableQrCode)
        navActions.navigateToFaceRecognition()
    }

    NavHost(
        navController = navController,
        startDestination = CustomerScreen.FaceRecognition.name,
    ) {
        composable(route = CustomerScreen.FaceRecognition.name) {
            FaceRecognitionScreen(
                navigateToWelcomeScreen = { navActions.navigateToWelcome() },
                navigateToCannotRecognize = { navActions.navigateToCannotRecognize() },
                navigateToWaitForConfirmation = { navActions.navigateToWaitForCheckInConfirmation() },
            )
        }
        composable(route = CustomerScreen.CannotRecognize.name) {
            CannotRecognizeScreen(
                onCancelButtonClicked = { navigateToFaceRecognition() },
                onScanButtonClicked = { navigateToFaceRecognition(enableQrCode = true) },
                onRegisterButtonClicked = { navActions.navigateToAgreement() },
            )
        }
        composable(route = CustomerScreen.Agreement.name) {
            AgreementScreen(
                onCancelButtonClicked = { navigateToFaceRecognition() },
                onCheckInButtonClicked = { navigateToFaceRecognition() },
                onAgreeButtonClicked = {
                    if (UserInfoViewModel.uiState.value.qrCodeScanned) {
                        navigateToFaceRecognition(shouldResetForm = false)
                    } else {
                        navActions.navigateToUserInfo()
                        UserInfoViewModel.resetNames()
                    }
                },
            )
        }
        composable(route = CustomerScreen.UserInfo.name) {
            UserInfoScreen(
                onCancelButtonClicked = { navigateToFaceRecognition() },
                onContinueButtonClicked = { navActions.navigateToWaitForConfirmation() }
            )
        }
        composable(route = CustomerScreen.WaitForConfirmation.name) {
            WaitForConfirmationScreen(
                onCancelButtonClicked = { navigateToFaceRecognition() },
            )
        }
        composable(route = CustomerScreen.AccessGranted.name) {
            AccessGrantedScreen(
                onCancelButtonClicked = { navigateToFaceRecognition() }
            )
        }
        composable(route = CustomerScreen.MaintenanceScreen.name) {
            MaintenanceScreen()
        }
        composable(route = CustomerScreen.WelcomeScreen.name) {
            WelcomeScreen(
                onCancelButtonClicked = { navigateToFaceRecognition() }
            )
        }
    }
}
