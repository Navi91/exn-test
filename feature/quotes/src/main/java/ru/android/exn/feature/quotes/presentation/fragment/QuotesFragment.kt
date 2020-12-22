package ru.android.exn.feature.quotes.presentation.fragment

import androidx.fragment.app.Fragment
import ru.android.exn.feature.quotes.di.DaggerQuotesFragmentComponent
import ru.android.exn.feature.quotes.di.QuotesFragmentComponent
import ru.android.exn.feature.quotes.di.QuotesFragmentDependency

class QuotesFragment : Fragment() {

    private val component: QuotesFragmentComponent by lazy {
        DaggerQuotesFragmentComponent
            .factory()
            .create(
                (requireActivity().application as QuotesFragmentDependency.DependencyProvider)
                    .getQuotesFragmentDependency()
            )
    }

}