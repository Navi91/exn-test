package ru.android.exn.feature.quotes.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.android.exn.feature.quotes.databinding.FragmentQuotesBinding
import ru.android.exn.feature.quotes.di.DaggerQuotesFragmentComponent
import ru.android.exn.feature.quotes.di.QuotesFragmentComponent
import ru.android.exn.feature.quotes.di.QuotesFragmentDependency
import ru.android.exn.feature.quotes.presentation.viewmodel.QuotesViewModel
import javax.inject.Inject

class QuotesFragment : Fragment() {

    private val component: QuotesFragmentComponent by lazy {
        DaggerQuotesFragmentComponent
            .factory()
            .create(
                (requireActivity() as QuotesFragmentDependency.DependencyProvider)
                    .getQuotesFragmentDependency()
            )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: QuotesViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[QuotesViewModel::class.java]
    }

    private var _binding: FragmentQuotesBinding? = null
    private val binding: FragmentQuotesBinding
        get() = _binding ?: error("FragmentQuotesBinding is null")

    private val root: View
        get() = binding.root

    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuotesBinding.inflate(inflater, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.back()
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}