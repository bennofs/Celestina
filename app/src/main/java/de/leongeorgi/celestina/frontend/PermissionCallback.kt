package de.leongeorgi.celestina.frontend

/**
 * @author leon
 */
interface PermissionCallback {
    fun onPermissionGranted()
    fun onPermissionDenied(deniedPermissions: Array<String>)
}