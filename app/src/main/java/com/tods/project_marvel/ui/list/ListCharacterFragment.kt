package com.tods.project_marvel.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tods.project_marvel.R
import com.tods.project_marvel.ui.base.BaseFragment
import com.tods.project_marvel.databinding.FragmentListCharacterBinding
import com.tods.project_marvel.state.ResourceState
import com.tods.project_marvel.ui.adapters.CharacterAdapter
import com.tods.project_marvel.util.hide
import com.tods.project_marvel.util.show
import com.tods.project_marvel.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ListCharacterFragment: BaseFragment<FragmentListCharacterBinding, ListCharacterViewModel>() {
    override val viewModel: ListCharacterViewModel by viewModels()
    private val characterAdapter by lazy { CharacterAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecyclerView()
        configClickAdapter()
        configCollectObserver()
    }

    private fun configCollectObserver() = lifecycleScope.launch() {
        viewModel.list.collect { resource ->
            when(resource) {
                is ResourceState.Success -> {
                    resource.data?.let { values ->
                        binding.progressCircular.hide()
                        characterAdapter.characters = values.data.results.toList()
                    }
                }
                is ResourceState.Error -> {
                    binding.progressCircular.hide()
                    resource.message?.let { message ->
                        toast(getString(R.string.an_error_occurred))
                        Timber.tag("ListCharacterFragment").e("Error: $message")
                    }
                }
                is ResourceState.Loading -> {
                    binding.progressCircular.show()
                }
                else -> { }
            }
        }
    }

    private fun configRecyclerView() = with(binding) {
        rvCharacters.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun configClickAdapter() {
        characterAdapter.setOnClickListener { characterModel ->
            val action = ListCharacterFragmentDirections
                .actionListCharacterFragmentToDetailsCharacterFragment(characterModel)
            findNavController().navigate(action)
        }
    }

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentListCharacterBinding = FragmentListCharacterBinding.inflate(inflater, container, false)
}