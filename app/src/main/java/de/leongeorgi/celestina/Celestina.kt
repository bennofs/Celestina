package de.leongeorgi.celestina

import android.app.Application
import de.leongeorgi.celestina.backend.loader.MusicLoader
import de.leongeorgi.celestina.backend.model.Album
import de.leongeorgi.celestina.backend.model.Artist
import de.leongeorgi.celestina.backend.model.Song
import de.leongeorgi.celestina.frontend.CelestinaActivity

/**
 * @author leon
 */
class Celestina : Application() {
    var songs = emptyList<Song>()

    var albums = emptyList<Album>()

    var artists = emptyList<Artist>()

    var albumArtists = emptyList<Artist>()

    fun updateMusic(activity: CelestinaActivity) {
        val songs = MusicLoader.queryAllSongs(activity)

        this.songs = songs

        val unknownArtist = Artist(
                -1,
                applicationContext.getString(R.string.unknown_artist_name)
        )
        val variousArtist = Artist(
                0,
                applicationContext.getString(R.string.various_artists_name)
        )

        val albumArtists = mutableSetOf<Artist>()

        albums = songs.groupBy { it.albumId }.map { (id, songs) ->
            val artist = generateArtist(songs, unknownArtist, variousArtist)
            albumArtists.add(artist)
            Album(
                    id,
                    songs.first().albumTitle,
                    artist.id,
                    artist.name,
                    songs
            )
        }.sortedBy { it.artistName }

        this.albumArtists = albumArtists.sortedBy { it.name }

        artists = songs.groupBy { Artist(it.artistId, it.artistName) }.keys.sortedBy { it.name }
    }

    private fun generateArtist(
            songs: List<Song>,
            unknownArtist: Artist,
            variousArtists: Artist
    ): Artist {
        val artistSongCount = songs.groupBy { Artist(it.artistId, it.artistName) }
                .map { Pair(it.key, it.value.size) }
                .sortedByDescending { it.second }
        if (artistSongCount.isEmpty())
            return unknownArtist
        return if (artistSongCount.getOrNull(1) == null ||
                artistSongCount[0].second < artistSongCount[1].second * 1.5) {
            return when (artistSongCount[0].first.name) {
                "Various Artists" -> variousArtists
                variousArtists.name -> variousArtists
                "<unknown>" -> unknownArtist
                unknownArtist.name -> unknownArtist
                else -> artistSongCount[0].first
            }
        } else variousArtists
    }

}