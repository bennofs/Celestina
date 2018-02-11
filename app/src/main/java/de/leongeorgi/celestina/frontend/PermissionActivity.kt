package de.leongeorgi.celestina.frontend

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
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

        val requestCode = UUID.randomUUID().mostSignificantBits.toInt()
        permissionCallbacks.put(requestCode, callback)

        ActivityCompat.requestPermissions(this, notGranted.toTypedArray(), requestCode)
    }

    fun defaultPermissionCallback(grantedFunction: () -> Unit) = object : PermissionCallback {
        override fun onPermissionDenied(deniedPermissions: Array<String>) {
            TODO("open a dialog")
        }

        override fun onPermissionGranted() = grantedFunction()
    }

}