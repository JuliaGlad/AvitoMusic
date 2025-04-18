package myapplication.android.musicavito.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import myapplication.android.musicavito.data.api.TracksApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RetrofitModule::class
    ]
)
interface AppComponent {

    fun httpClient(): OkHttpClient

    fun videoRetrofit(): Retrofit

    fun provideApi(): TracksApi

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

}