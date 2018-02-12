package de.leongeorgi.celestina.backend.model

/**
 * @author leon
 */
data class Song(
        val id: Long,
        val title: String,
        val albumId: Long,
        val albumTitle: String,
        val artistId: Long,
        val artistName: String,
        val duration: Long,
        val trackNumber: Int,
        val filePath: String,
        val dateAdded: Long,
        val dateModified: Long,
        val releaseYear: Int,
        val composer: String?,
        val bookmark: Long,
        val isMusic: Boolean
)