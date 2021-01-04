package ru.android.exn.feature.quotes.presentation.fragment

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.android.exn.feature.quotes.R
import ru.android.exn.feature.quotes.databinding.FragmentQuotesBinding
import ru.android.exn.feature.quotes.di.DaggerQuotesFragmentComponent
import ru.android.exn.feature.quotes.di.QuotesFragmentComponent
import ru.android.exn.feature.quotes.di.QuotesFragmentDependency
import ru.android.exn.feature.quotes.presentation.adapter.QuotesAdapter
import ru.android.exn.feature.quotes.presentation.adapter.QuotesDiffUtilCallback
import ru.android.exn.feature.quotes.presentation.adapter.QuotesItemTouchHelperCallback
import ru.android.exn.feature.quotes.presentation.model.SocketStatusModel
import ru.android.exn.feature.quotes.presentation.viewmodel.QuotesViewModel
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
    private val quotesRecyclerView: RecyclerView
        get() = binding.quotesRecyclerView
    private val emptyMessageTextView: TextView
        get() = binding.emptyMessageTextView

    private val adapter: QuotesAdapter by lazy { QuotesAdapter() }

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

        setupQuotesRecyclerView()
        setupBackPressedCallback()

        observeConnectionStatus()
        observeQuotes()
    }

    override fun onStart() {
        super.onStart()

        viewModel.processStart()
    }

    override fun onStop() {
        super.onStop()

        viewModel.processStop()
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

    private fun observeConnectionStatus() {
        viewModel.socketStatus.observe(viewLifecycleOwner, { socketStatus ->
            when (socketStatus) {
                SocketStatusModel.CONNECTING -> {
                    showStatusTextView()
                }
                SocketStatusModel.CONNECTED -> {
                    hideStatusTextView()
                }
            }
        })
    }

    private fun observeQuotes() {
        viewModel.model.observe(viewLifecycleOwner, { quotes ->
            val diffResult = DiffUtil.calculateDiff(QuotesDiffUtilCallback(adapter.items, quotes))

            adapter.setItems(quotes)

            diffResult.dispatchUpdatesTo(adapter)

            emptyMessageTextView.isVisible = quotes.isEmpty()
        })
    }

    private fun setupQuotesRecyclerView() {
        val touchHelperCallback = QuotesItemTouchHelperCallback(
            { fromPosition, toPosition -> viewModel.processMove(fromPosition, toPosition) },
            { swipedPosition -> viewModel.processSwipe(swipedPosition) }
        )
        ItemTouchHelper(touchHelperCallback).attachToRecyclerView(quotesRecyclerView)
        quotesRecyclerView.adapter = adapter
    }

    private fun setupBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.back()
                }
            }
        )
    }

    private fun showStatusTextView() {
        ValueAnimator.ofInt(
            statusTextView.height,
            resources.getDimensionPixelSize(R.dimen.status_text_view_height)
        ).apply {
            addUpdateListener {
                val newHeight = (it.animatedValue as Int)
                statusTextView.layoutParams = statusTextView.layoutParams.apply {
                    height = newHeight
                }
            }
        }
            .start()
    }

    private fun hideStatusTextView() {
        ValueAnimator.ofInt(statusTextView.height, 0).apply {
            addUpdateListener {
                val newHeight = (it.animatedValue as Int)
                statusTextView.layoutParams = statusTextView.layoutParams.apply {
                    height = newHeight
                }
            }
        }
            .start()
    }
}