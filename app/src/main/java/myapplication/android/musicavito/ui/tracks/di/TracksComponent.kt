package myapplication.android.musicavito.ui.tracks.di

import dagger.Component
import myapplication.android.musicavito.di.AppComponent
import myapplication.android.musicavito.ui.tracks.TracksFragment
import javax.inject.Scope

@TracksScope
@Component(
    modules = [
        TracksModule::class,
        TracksLocalDIModule::class
    ],
    dependencies = [AppComponent::class]
)
interface TracksComponent {

    fun inject(fragment: TracksFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): TracksComponent
    }

}

@Scope
annotation class TracksScope