package myapplication.android.musicavito.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import myapplication.android.musicavito.data.api.TracksApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RetrofitModule {

    private val jsonSerializer = Json { ignoreUnknownKeys = true }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): TracksApi =
        retrofit.create(TracksApi::class.java)

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()

    @Singleton
    @Provides
    fun provideVideoRetrofit(authClient: OkHttpClient): Retrofit =
        Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            addConverterFactory(
                jsonSerializer.asConverterFactory(
                    JSON.toMediaType()
                )
            )
            client(authClient)
        }.build()

    companion object {
        const val JSON = "application/json; charset=UTF8"
        const val BASE_URL = "https://api.deezer.com/"
    }

}