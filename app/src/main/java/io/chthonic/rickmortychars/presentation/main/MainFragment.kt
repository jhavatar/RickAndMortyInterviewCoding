package io.chthonic.rickmortychars.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.chthonic.rickmortychars.databinding.MainFragmentBinding
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: MainFragmentBinding

    private val viewModel: MainViewModel by viewModels()
    private val characterInfoListAdapter: CharacterInfoListAdapter by lazy {
        CharacterInfoListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        collectUiState()
    }

    private fun initView() {
        val layoutManager = GridLayoutManager(activity, 2)
        binding.charsListView.layoutManager = layoutManager
        binding.charsListView.adapter = characterInfoListAdapter
    }

    private fun collectUiState() {
        lifecycleScope.launchWhenStarted {
            characterInfoListAdapter.loadStateFlow.collectLatest { loadStates ->
                viewModel.onLoadingStatesChanged(loadStates)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loadingIsVisible.collect { loadingIsVisible ->
                binding.loadingView.isVisible = loadingIsVisible
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.characterInfosToDisplay.collect { characterInfoPagingData ->
                characterInfoListAdapter.submitData(characterInfoPagingData)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.errorMessageToDisplay.collect { errorMessage ->
                if (errorMessage != null) {
                    displayError(errorMessage)
                    viewModel.onErrorDisplayed()
                }
            }
        }
    }

    private fun displayError(errorMessage: String) {
        Snackbar.make(binding.main, errorMessage, Snackbar.LENGTH_LONG).show()
    }
}