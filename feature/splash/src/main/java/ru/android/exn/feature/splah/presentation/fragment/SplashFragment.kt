package ru.android.exn.feature.splah.presentation.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.android.exn.feature.splah.R
import ru.android.exn.feature.splah.di.DaggerSplashFragmentComponent
import ru.android.exn.feature.splah.di.SplashFragmentComponent
import ru.android.exn.feature.splah.di.SplashFragmentDependency
import ru.android.exn.feature.splah.presentation.viewmodel.SplashViewModel
import javax.inject.Inject

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val component: SplashFragmentComponent by lazy {
        DaggerSplashFragmentComponent
            .factory()
            .create(
                (requireActivity() as SplashFragmentDependency.DependencyProvider)
                    .getSplashFragmentDependency()
            )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
    }
}