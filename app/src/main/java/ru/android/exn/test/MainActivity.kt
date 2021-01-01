package ru.android.exn.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.android.exn.basic.navigation.ExitNavEvent
import ru.android.exn.basic.navigation.NavEventProvider
import ru.android.exn.basic.navigation.UpNavEvent
import ru.android.exn.feature.quotes.di.QuotesFragmentDependency
import ru.android.exn.feature.quotes.presentation.fragment.QuotesFragmentDirections
import ru.android.exn.feature.quotes.presentation.navigation.OpenSettingsScreenFromQuotesScreen
import ru.android.exn.feature.settings.di.SettingsFragmentDependency
import ru.android.exn.test.databinding.ActivityMainBinding
import ru.android.exn.test.di.activity.DaggerMainActivityComponent
import ru.android.exn.test.di.activity.MainActivityComponent
import ru.android.exn.test.di.activity.MainActivityDependency
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
        QuotesFragmentDependency.DependencyProvider,
        SettingsFragmentDependency.DependencyProvider {

    //{"ticks":[{"s":"BTCUSD","b":"27271.04","bf":1,"a":"27286.39","af":2,"spr":"153.5"},{"s":"BTCUSD","b":"27265.07","bf":2,"a":"27285.35","af":2,"spr":"202.8"}]}

    private val component: MainActivityComponent by lazy {
        DaggerMainActivityComponent
                .factory()
                .create((application as MainActivityDependency.DependencyProvider).getMainActivityDependency())
    }

    @Inject
    lateinit var navEventProvider: NavEventProvider

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy {
        findNavController(R.id.navigation_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupNavController()

        observeNavEvents()
    }

    private fun setupNavController() {
        navController.setGraph(R.navigation.navigation)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d(LOG_TAG, "Destination: $destination")

            when (destination.id) {
                R.id.quotesFragment -> {
                    supportActionBar?.setTitle(R.string.quotes_title)
                }
                R.id.settingsFragment -> {
                    supportActionBar?.setTitle(R.string.settings_title)
                }
            }
        }
    }

    private fun observeNavEvents() {
        navEventProvider.getEvents().observe(this, { event ->
            when (event) {
                ExitNavEvent                       -> finish()
                UpNavEvent                         -> {
                    if (!navController.navigateUp()) {
                        finish()
                    }
                }
                OpenSettingsScreenFromQuotesScreen -> {
                    navController.navigate(
                            QuotesFragmentDirections.actionQuotesFragmentToSettingsFragment()
                    )
                }
                else                               -> {
                    Log.w(LOG_TAG, "Unexpected nav event: $event")
                }
            }
        })
    }

    override fun getQuotesFragmentDependency(): QuotesFragmentDependency =
            component

    override fun getSettingsFragmentDependency(): SettingsFragmentDependency =
            component

    companion object {

        const val LOG_TAG = "MainActivity"
    }
}