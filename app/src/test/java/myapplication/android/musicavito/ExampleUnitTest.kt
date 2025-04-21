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
class ExampleUnitTest {
    private val downloadTracksReducer: DownloadedTracksReducer = DownloadedTracksReducer()

    @Test
    fun checkSearch() {
        val reduce: DownloadedTracksState = downloadTracksReducer.reduce(
            partialState = DownloadedTracksPartialState.FilterData("2"),
            prevState = DownloadedTracksState(
                content = LceState.Content(
                    LocalTracksList(
                        tracks = listOf(
                            TrackUi(
                                1,
                                "Title 1",
                                "audioUri",
                                AlbumUi("someAlbum", "imageUri"),
                                ArtistUi("artist"),
                                isDownloaded = true
                            ),
                            TrackUi(
                                2,
                                "Title 2",
                                "audioUri",
                                AlbumUi("someAlbum", "imageUri"),
                                ArtistUi("artist"),
                                isDownloaded = true
                            ),
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
                        ),
                        filteredTracks = emptyList()
                    )
                )
            )
        )
        val expectedValue = listOf(
            TrackUi(
                2,
                "Title 2",
                "audioUri",
                AlbumUi("someAlbum", "imageUri"),
                ArtistUi("artist"),
                isDownloaded = true
            )
        )
        assertEquals(((reduce.content) as LceState.Content).data.filteredTracks, expectedValue)
    }
}