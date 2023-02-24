package com.example.lobbyapp.ui.customer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.util.*

enum class CustomerScreen {
    Agreement,
    FaceRecognition,
    CannotRecognize,
    UserInfo,
    WaitForConfirmation,
    AccessGranted,
    MaintenanceScreen,
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
    NavHost(
        navController = navController,
        startDestination = CustomerScreen.FaceRecognition.name,
    ) {
        composable(route = CustomerScreen.FaceRecognition.name) {
            FaceRecognitionScreen(
                navigateToWaitForConfirmation = { navActions.navigateToWaitForCheckInConfirmation() },
                navigateToCannotRecognize = { navActions.navigateToCannotRecognize() },
            )
        }
        composable(route = CustomerScreen.CannotRecognize.name) {
            CannotRecognizeScreen(
                onCancelButtonClicked = { navActions.navigateToFaceRecognition() },
                onTryAgainButtonClicked = { navActions.navigateToFaceRecognition() },
                onRegisterButtonClicked = { navActions.navigateToAgreement() },
            )
        }
        composable(route = CustomerScreen.Agreement.name) {
            AgreementScreen(
                onCancelButtonClicked = { navActions.navigateToFaceRecognition() },
                onCheckInButtonClicked = { navActions.navigateToFaceRecognition() },
                onSendButtonClicked = { navActions.navigateToUserInfo() },
            )
        }
        composable(route = CustomerScreen.UserInfo.name) {
            UserInfoScreen(
                onCancelButtonClicked = { navActions.navigateToFaceRecognition() },
                onContinueButtonClicked = { navActions.navigateToWaitForConfirmation() }
            )
        }
        composable(route = CustomerScreen.WaitForConfirmation.name) {
            WaitForConfirmationScreen(
                onCancelButtonClicked = { navActions.navigateToFaceRecognition() }
            )
        }
        composable(route = CustomerScreen.AccessGranted.name) {
            AccessGrantedScreen(
                onCancelButtonClicked = { navActions.navigateToFaceRecognition() }
            )
        }
        composable(route = CustomerScreen.MaintenanceScreen.name) {
            MaintenanceScreen()
        }
    }
}
