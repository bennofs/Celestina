package de.leongeorgi.celestina.frontend

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import de.leongeorgi.celestina.R
import java.util.*


abstract class PermissionActivity : AppCompatActivity() {

    val permissionCallbacks = mutableMapOf<Int, PermissionCallback>()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (!grantResults.contains(PackageManager.PERMISSION_DENIED)) {
            // permission is granted
            permissionCallbacks[requestCode]?.onPermissionGranted()
        } else {
            // some permissions were denied
            val deniedPermissions = permissions.zip(grantResults.toTypedArray())
                    .filter { it.second == PackageManager.PERMISSION_DENIED }
                    .map { it.first }
                    .toTypedArray()
            permissionCallbacks[requestCode]?.onPermissionDenied(deniedPermissions)
        }
        // remove callback
        permissionCallbacks.remove(requestCode)
    }

    fun isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(this,
            permission) == PackageManager.PERMISSION_GRANTED

    fun requirePermission(vararg permissionTypes: String, callback: PermissionCallback) {
        val notGranted = permissionTypes.filter { !isPermissionGranted(it) }
        if (notGranted.isEmpty()) {
            // all required permissions are granted
            callback.onPermissionGranted()
            return
        }

        Log.i("not granted permissions", notGranted.toString())

        // 16 bit random int as request id
        val requestCode = UUID.randomUUID().mostSignificantBits.toInt() and 0xFFFF

        permissionCallbacks.put(requestCode, callback)

        ActivityCompat.requestPermissions(this, notGranted.toTypedArray(), requestCode)
    }

    fun requirePermission(vararg permissionTypes: String, grantedFunction: () -> Unit) =
            requirePermission(
                    *permissionTypes,
                    callback = defaultPermissionCallback(grantedFunction)
            )

    private fun defaultPermissionCallback(grantedFunction: () -> Unit): PermissionCallback = object : PermissionCallback {
        override fun onPermissionDenied(deniedPermissions: Array<String>) {
            // TODO: theme
            AlertDialog.Builder(this@PermissionActivity)
                    .setTitle(R.string.dialog_permission_denied_title)
                    .setMessage(R.string.dialog_permission_denied_message)
                    .setPositiveButton(R.string.dialog_permission_denied_button_positive) { _, _ ->
                        requirePermission(
                                *deniedPermissions,
                                callback = defaultPermissionCallback(grantedFunction)
                        )
                    }
                    .setNeutralButton(R.string.dialog_permission_denied_button_neutral) { _, _ ->
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .setNegativeButton(R.string.dialog_permission_denied_button_negative) { _, _ ->
                        finish()
                    }.create().show()

        }

        override fun onPermissionGranted() = grantedFunction()
    }

}