package ru.android.exn.feature.quotes.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.android.exn.feature.quotes.R
import ru.android.exn.feature.quotes.databinding.FragmentQuotesBinding
import ru.android.exn.feature.quotes.di.DaggerQuotesFragmentComponent
import ru.android.exn.feature.quotes.di.QuotesFragmentComponent
import ru.android.exn.feature.quotes.di.QuotesFragmentDependency
import ru.android.exn.feature.quotes.presentation.viewmodel.QuotesViewModel
import ru.android.exn.shared.quotes.domain.entity.SocketStatus
import javax.inject.Inject

internal class QuotesFragment : Fragment() {

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
    private val statusTextView: TextView
        get() = binding.statusTextView

    override fun onAttach(context: Context) {
        component.inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
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

        viewModel.socketStatus.observe(viewLifecycleOwner, { socketStatus ->
            when (socketStatus) {
                SocketStatus.CREATED    -> statusTextView.text = "created"
                SocketStatus.CONNECTING -> statusTextView.text = "Connecting"
                SocketStatus.OPEN       -> statusTextView.text = "open"
                SocketStatus.CLOSING    -> statusTextView.text = "closing"
                SocketStatus.CLOSED     -> statusTextView.text = "closed"
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.quotes_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            viewModel.openSettings()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}