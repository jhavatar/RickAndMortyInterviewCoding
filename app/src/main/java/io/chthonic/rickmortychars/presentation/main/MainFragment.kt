package io.chthonic.rickmortychars.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.chthonic.rickmortychars.databinding.MainFragmentBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    lateinit var binding: MainFragmentBinding

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
        val layoutManager = GridLayoutManager(activity, 2)
        binding.charsListView.layoutManager = layoutManager
        binding.charsListView.adapter = characterInfoListAdapter

        lifecycleScope.launchWhenStarted {
            viewModel.loadingIsVisible.collect { isLoadingVisible ->
                binding.loadingView.visibility = if (isLoadingVisible) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.characterInfosToDisplay.collect { characterInfoList ->
                characterInfoListAdapter.submitList(characterInfoList)
            }
        }
    }
}