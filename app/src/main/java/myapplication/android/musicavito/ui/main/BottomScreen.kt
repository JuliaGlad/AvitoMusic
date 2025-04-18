package myapplication.android.musicavito.ui.main

import com.github.terrakok.cicerone.androidx.FragmentScreen
import myapplication.android.musicavito.ui.downloaded_tracks.DownloadedTracksFragment
import myapplication.android.musicavito.ui.tracks.TracksFragment

object BottomScreen {
    fun general() = FragmentScreen { TracksFragment() }
    fun downloaded() = FragmentScreen { DownloadedTracksFragment() }
}