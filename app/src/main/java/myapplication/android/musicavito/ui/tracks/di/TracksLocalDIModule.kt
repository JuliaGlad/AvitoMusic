package myapplication.android.musicavito.ui.tracks.di

import dagger.Module
import dagger.Provides
import myapplication.android.musicavito.data.repository.TracksRepository
import myapplication.android.musicavito.ui.tracks.mvi.TracksLocalDi

@Module
class TracksLocalDIModule {

    @TracksScope
    @Provides
    fun provideTracksLocalDI(
        tracksRepository: TracksRepository
    ): TracksLocalDi = TracksLocalDi(tracksRepository)

}