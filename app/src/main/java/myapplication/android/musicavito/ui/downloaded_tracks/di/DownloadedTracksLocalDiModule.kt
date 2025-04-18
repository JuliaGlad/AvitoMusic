package myapplication.android.musicavito.ui.downloaded_tracks.di

import dagger.Module
import dagger.Provides
import myapplication.android.musicavito.data.repository.TracksRepository
import myapplication.android.musicavito.ui.downloaded_tracks.mvi.DownloadedTracksLocalDi

@Module
class DownloadedTracksLocalDiModule {

    @DownloadedTracksScope
    @Provides
    fun provideDownloadedTracksLocalDI(
        tracksRepository: TracksRepository
    ):  DownloadedTracksLocalDi = DownloadedTracksLocalDi(tracksRepository)

}