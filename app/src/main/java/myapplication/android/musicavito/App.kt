package myapplication.android.musicavito

import android.app.Application
import androidx.room.Room.databaseBuilder
import com.github.terrakok.cicerone.Cicerone
import myapplication.android.musicavito.data.database.LocalDatabase

class App: Application() {

    private val cicerone = Cicerone.create()
    val database: LocalDatabase by lazy { createDatabase() }
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    private fun createDatabase() =
        databaseBuilder(this, LocalDatabase::class.java, "database")
            .allowMainThreadQueries()
            .build()

    companion object {
        internal lateinit var app: App
            private set
    }

}