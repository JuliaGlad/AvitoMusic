package myapplication.android.musicavito.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.AppNavigator
import myapplication.android.musicavito.App.Companion.app
import myapplication.android.musicavito.R
import myapplication.android.musicavito.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val navigator: AppNavigator = AppNavigator(this, R.id.main_container)
    private val presenter: MainPresenter by lazy { MainPresenter(app.router) }
    private val navigationHolder: NavigatorHolder by lazy { app.navigatorHolder }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomBar()
        if (savedInstanceState == null) {
            presenter.setupRootFragment(BottomScreen.general())
        }
    }

    private fun initBottomBar() {
        binding.bottomNav.itemIconTintList = null
        binding.bottomNav.setOnItemSelectedListener { item ->
            val screen: Screen? = when (item.itemId) {
                R.id.action_general -> BottomScreen.general()
                R.id.action_downloaded -> BottomScreen.downloaded()
                else -> null
            }
            screen?.let { presenter.navigateTo(it) }
            true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}