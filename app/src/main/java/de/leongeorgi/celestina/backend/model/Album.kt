package de.leongeorgi.celestina.backend.model

/**
 * @author leon
 */
data class Album(
        val id: Long,
        val name: String,
        val artistId: Long,
        val artistName: String
) {
}