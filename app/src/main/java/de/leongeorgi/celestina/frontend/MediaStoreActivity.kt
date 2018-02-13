package de.leongeorgi.celestina.frontend

import android.Manifest
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import de.leongeorgi.celestina.backend.loader.MusicLoader
import de.leongeorgi.celestina.debug


/**
 * @author leon
 */
abstract class MediaStoreActivity : PermissionActivity() {

    var pauseTime: Long = 0

    val contentObserver =
            object : ContentObserver(Handler()) {
                override fun onChange(selfChange: Boolean) {
                    debug("MediaStore change detected")
                    onMediaStoreChanged()
                }
            }

    var observerRegistered = false

    override fun onPause() {
        super.onPause()
        pauseTime = System.currentTimeMillis() / 1000
        unregisterObserver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pauseTime = System.currentTimeMillis() / 1000
        requirePermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            celestina.updateMusic(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            val lastAddedSong = MusicLoader.queryLastAddedSong(this)
            val lastModifiedSong = MusicLoader.queryLastModifiedSong(this)
            if (lastAddedSong?.dateAdded ?: 0 > pauseTime ||
                    lastModifiedSong?.dateModified ?: 0 > pauseTime) {
                notifyMediaStoreChanged()
            }
            registerObserver()
        }
    }

    private fun registerObserver() {
        if (!observerRegistered)
            contentResolver.registerContentObserver(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    true,
                    contentObserver
            )
        observerRegistered = true
    }

    private fun unregisterObserver() {
        if (observerRegistered) {
            contentResolver.unregisterContentObserver(contentObserver)
        }
        observerRegistered = false
    }

    private fun notifyMediaStoreChanged() {
        debug("MediaStore changed event")
        onMediaStoreChanged()
    }

    fun onMediaStoreChanged() {
        celestina.updateMusic(this)
    }

    abstract fun onMusicUpdated()
}