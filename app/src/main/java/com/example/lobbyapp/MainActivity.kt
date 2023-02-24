package com.example.lobbyapp

import android.Manifest
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.example.lobbyapp.ui.customer.CustomerActivity
import com.example.lobbyapp.ui.operator.OperatorActivity
import com.example.lobbyapp.util.checkConfigExists
import com.example.lobbyapp.util.checkConfigRequiredFieldsExist
import com.example.lobbyapp.util.checkInternetAvailable
import com.vmadalin.easypermissions.EasyPermissions

class MainActivity : ComponentActivity(), EasyPermissions.PermissionCallbacks {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()

        if (!hasProperPermissions()) {
            requestProperPermissions()
        } else {
            start()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        if (!hasProperPermissions()) {
            requestProperPermissions()
        } else {
            start()
        }
    }

    override fun onStop() {
        super.onStop()

        finish()
    }

    private fun hasProperPermissions() =
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.STATUS_BAR
        )

    private fun requestProperPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "This application needs these permissions.",
            PERMISSION_REQUEST_CODE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.STATUS_BAR
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (hasProperPermissions()) {
            start()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "Please grant permissions", Toast.LENGTH_LONG).show()

            // open app settings
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } else {
            requestProperPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun start() {
        // Get the DisplayManager
        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        // Get all connected displays
        val displays = displayManager.displays
        // Check if the config file exists
        val isConfigExisted = checkConfigExists()
        // Check if the required fields in config file exist
        val isRequiredFieldsExisted = checkConfigRequiredFieldsExist()
        // Check if the device is connecting to the internet
        val isInternetAvailable = checkInternetAvailable(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (display in displays) {
                val options = ActivityOptions.makeBasic()
                options.launchDisplayId = display.displayId

                if (display.displayId == displays[0].displayId) {
                    // Start CustomerActivity on the front display
                    val intent = Intent(this, CustomerActivity::class.java)
                    intent.putExtra("isConfigExisted", isConfigExisted)
                    intent.putExtra("isInternetAvailable", isInternetAvailable)
                    intent.putExtra("isRequiredFieldsExisted", isRequiredFieldsExisted)
                    startActivity(intent, options.toBundle())
                } else {
                    // Start OperatorActivity on the back display
                    val intent = Intent(this, OperatorActivity::class.java)
                    intent.putExtra("isConfigExisted", isConfigExisted)
                    intent.putExtra("isInternetAvailable", isInternetAvailable)
                    intent.putExtra("isRequiredFieldsExisted", isRequiredFieldsExisted)
                    startActivity(intent, options.toBundle())
                }
            }
        }
    }
}

