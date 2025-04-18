package myapplication.android.musicavito.ui.downloaded_tracks.di

import dagger.Component
import myapplication.android.musicavito.di.AppComponent
import myapplication.android.musicavito.ui.downloaded_tracks.DownloadedTracksFragment
import javax.inject.Scope

@DownloadedTracksScope
@Component(
    modules = [
        DownloadedTracksLocalDiModule::class,
        DownloadedTracksModule::class
    ],
    dependencies = [AppComponent::class]
)
interface DownloadedTracksComponent {

    fun inject(fragment: DownloadedTracksFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): DownloadedTracksComponent
    }

}

@Scope
annotation class DownloadedTracksScope