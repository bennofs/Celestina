package de.leongeorgi.celestina.backend.model

import android.content.ContentUris
import android.net.Uri
import java.util.*

/**
 * @author leon
 */
data class Album(
        val id: Long,
        val name: String,
        val artistId: Long,
        val artistName: String,
        val songs: List<Song>
) {
    val songCount by lazy { songs.size }

    val releaseYear by lazy {
        songs.map { it.releaseYear }.filter { it > 0 }.min() ?:
                Calendar.getInstance().get(Calendar.YEAR)
    }

    val dateAdded by lazy { songs.map { it.dateAdded }.min() }
    val dateLastAdded by lazy { songs.map { it.dateAdded }.max() }

    val coverPath by lazy {
        ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                id
        )
    }
}