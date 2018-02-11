package de.leongeorgi.celestina.backend.loader

import android.database.Cursor
import android.provider.MediaStore
import de.leongeorgi.celestina.frontend.PermissionActivity

/**
 * @author leon
 */
object SongLoader {
    @Throws(SecurityException::class)
    private fun makeCursor(
            activity: PermissionActivity,
            selection: String? = null,
            selectionArgs: Array<String>? = null,
            sortOrder: String = "${MediaStore.Audio.Media.TITLE} ASC"
    ): Cursor? {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DATE_MODIFIED,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.IS_MUSIC,
                MediaStore.Audio.Media.COMPOSER
        )
        return activity.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
    }
}