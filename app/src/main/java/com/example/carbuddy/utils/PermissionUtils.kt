package com.example.carbuddy.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionUtils{
    private const val MAX_PERMISSION_REQUESTS = 2
    private const val PERMISSION_PREF_NAME = "PermissionPrefs"
    private const val PERMISSION_REQUEST_COUNT_KEY = "permissionRequestCount"
    private const val PERMISSION_ALERT_TEXT = "Some permissions are still not granted. Please go to settings to enable them."

    private fun areAllPermissionsGranted(context: Context, permissions: Array<String> ) = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    fun handlePermissions(activity: Activity, permissions: Array<String>, requestCode: Int): Boolean {
        val sharedPreferences = activity.getSharedPreferences(PERMISSION_PREF_NAME, Context.MODE_PRIVATE)
        var permissionRequestCount = sharedPreferences.getInt(PERMISSION_REQUEST_COUNT_KEY, 0)
        return when {
            areAllPermissionsGranted(activity, permissions) -> true
            else -> {
                if (permissionRequestCount < MAX_PERMISSION_REQUESTS) {
                    sharedPreferences.edit()
                        .putInt(PERMISSION_REQUEST_COUNT_KEY, ++permissionRequestCount).apply()
                    requestPermissions(activity, permissions, requestCode)
                } else {
                    Dialogs.permissionAlertDialog(
                        activity,
                        PERMISSION_ALERT_TEXT
                    ) { openAppSettings(activity) }
                }
                false
            }
        }
    }
}


