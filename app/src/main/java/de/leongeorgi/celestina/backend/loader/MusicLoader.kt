package de.leongeorgi.celestina.backend.loader

import android.database.Cursor
import android.provider.MediaStore
import de.leongeorgi.celestina.backend.model.Song
import de.leongeorgi.celestina.frontend.CelestinaActivity

/**
 * @author leon
 */
object MusicLoader {

    fun queryAllSongs(activity: CelestinaActivity) =
            makeCursor(activity)?.let { cursor ->
                cursor.moveToFirst()
                return@let (1..cursor.count).map {
                    val song = makeSong(cursor)
                    cursor.moveToNext()
                    song
                }
            } ?: emptyList()

    fun queryLastAddedSong(activity: CelestinaActivity) =
            queryFirstSong(activity, "${MediaStore.Audio.Media.DATE_ADDED} DESC")

    fun queryLastModifiedSong(activity: CelestinaActivity) =
            queryFirstSong(activity, "${MediaStore.Audio.Media.DATE_MODIFIED} DESC")

    fun queryFirstSong(
            activity: CelestinaActivity,
            sortOrder: String = "${MediaStore.Audio.Media.TITLE} ASC"
    ): Song? {
        val cursor = makeCursor(activity, sortOrder = sortOrder)
        cursor?.moveToFirst() ?: return null
        return makeSong(cursor)
    }

    private fun makeSong(cursor: Cursor): Song = cursor.run {
        Song(
                getLong(0),
                getString(1),
                getLong(2),
                getString(3),
                getLong(4),
                getString(5),
                getLong(6),
                getInt(7),
                getString(8),
                getLong(9),
                getLong(10),
                getInt(11),
                getString(12),
                getLong(13),
                getInt(14) > 0
        )
    }

    private fun makeCursor(
            activity: CelestinaActivity,
            selection: String? = null,
            selectionArgs: Array<String>? = null,
            sortOrder: String = "${MediaStore.Audio.Media.TITLE} ASC"
    ): Cursor? {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DATE_MODIFIED,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.BOOKMARK,
                MediaStore.Audio.Media.IS_MUSIC
        )
        return activity.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
    }
}