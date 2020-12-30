package ru.android.exn.feature.settings.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.android.exn.feature.settings.databinding.FragmentSettingsBinding
import ru.android.exn.feature.settings.di.DaggerSettingsFragmentComponent
import ru.android.exn.feature.settings.di.SettingsFragmentComponent
import ru.android.exn.feature.settings.di.SettingsFragmentDependency
import ru.android.exn.feature.settings.presentation.viewmodel.SettingsViewModel
import javax.inject.Inject

internal class SettingsFragment : Fragment() {

    private val component: SettingsFragmentComponent by lazy {
        DaggerSettingsFragmentComponent
            .factory()
            .create(
                (requireActivity() as SettingsFragmentDependency.DependencyProvider)
                    .getSettingsFragmentDependency()
            )
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this, factory)[SettingsViewModel::class.java]
    }

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding ?: error("FragmentSettingsBinding is null")

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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

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