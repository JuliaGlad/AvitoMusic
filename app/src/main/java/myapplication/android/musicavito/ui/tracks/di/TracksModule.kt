package myapplication.android.musicavito.ui.tracks.di

import dagger.Binds
import dagger.Module
import myapplication.android.musicavito.data.repository.TracksRepository
import myapplication.android.musicavito.data.repository.TracksRepositoryImpl
import myapplication.android.musicavito.data.source.local.TracksLocalSource
import myapplication.android.musicavito.data.source.local.TracksLocalSourceImpl
import myapplication.android.musicavito.data.source.remote.TracksRemoteSource
import myapplication.android.musicavito.data.source.remote.TracksRemoteSourceImpl

@Module
interface TracksModule {

    @TracksScope
    @Binds
    fun bindTracksLocalSource(tracksLocalSourceImpl: TracksLocalSourceImpl): TracksLocalSource

    @TracksScope
    @Binds
    fun bindTracksRemoteSource(tracksRemoteSourceImpl: TracksRemoteSourceImpl): TracksRemoteSource

    @TracksScope
    @Binds
    fun bindTracksRepository(tracksRepositoryImpl: TracksRepositoryImpl): TracksRepository


}