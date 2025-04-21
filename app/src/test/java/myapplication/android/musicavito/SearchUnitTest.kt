package myapplication.android.musicavito

import myapplication.android.core_mvi.LceState
import myapplication.android.musicavito.ui.downloaded_tracks.model.LocalTracksList
import myapplication.android.musicavito.ui.downloaded_tracks.mvi.DownloadedTracksPartialState
import myapplication.android.musicavito.ui.downloaded_tracks.mvi.DownloadedTracksReducer
import myapplication.android.musicavito.ui.downloaded_tracks.mvi.DownloadedTracksState
import myapplication.android.musicavito.ui.model.AlbumUi
import myapplication.android.musicavito.ui.model.ArtistUi
import myapplication.android.musicavito.ui.model.TrackUi
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SearchUnitTest {
    private val downloadTracksReducer: DownloadedTracksReducer = DownloadedTracksReducer()
    private val trackTwo = TrackUi(
        2,
        "Title 2",
        "audioUri",
        AlbumUi("someAlbum", "imageUri"),
        ArtistUi("artist"),
        isDownloaded = true
    )
    private val fullList = listOf(
        TrackUi(
            1,
            "Title 1",
            "audioUri",
            AlbumUi("someAlbum", "imageUri"),
            ArtistUi("artist"),
            isDownloaded = true
        ),
        trackTwo,
        TrackUi(
            3,
            "Title 3",
            "audioUri",
            AlbumUi("someAlbum", "imageUri"),
            ArtistUi("artist"),
            isDownloaded = true
        ),
        TrackUi(
            4,
            "Title 4",
            "audioUri",
            AlbumUi("someAlbum", "imageUri"),
            ArtistUi("artist"),
            isDownloaded = true
        ),
    )
    @Test
    fun `check search with query = 2`() {
        val result = reduce("2")
        val expectedValue = listOf(trackTwo)
        assertEquals(((result.content) as LceState.Content).data.filteredTracks, expectedValue)
    }

    @Test
    fun `check search with empty query`() {
        val result = reduce("")
        val expectedValue = fullList
        assertEquals(((result.content) as LceState.Content).data.filteredTracks, expectedValue)
    }

    private fun reduce(query: String) = downloadTracksReducer.reduce(
        partialState = DownloadedTracksPartialState.FilterData(query),
        prevState = DownloadedTracksState(
            content = LceState.Content(
                LocalTracksList(
                    tracks = fullList,
                    filteredTracks = emptyList()
                )
            )
        )
    )
}