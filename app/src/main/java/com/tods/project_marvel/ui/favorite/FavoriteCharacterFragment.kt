package com.tods.project_marvel.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tods.project_marvel.R
import com.tods.project_marvel.data.model.character.CharacterModel
import com.tods.project_marvel.databinding.FragmentFavoriteCharacterBinding
import com.tods.project_marvel.state.ResourceState
import com.tods.project_marvel.ui.adapters.CharacterAdapter
import com.tods.project_marvel.ui.base.BaseFragment
import com.tods.project_marvel.util.hide
import com.tods.project_marvel.util.show
import com.tods.project_marvel.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteCharacterFragment: BaseFragment<FragmentFavoriteCharacterBinding, FavoriteCharacterViewModel>() {
    override val viewModel: FavoriteCharacterViewModel by viewModels()
    private val characterAdapter: CharacterAdapter by lazy { CharacterAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecyclerView()
        configClickAdapter()
        configCollectObserver()
    }

    private fun itemTouchHelperCallback(): ItemTouchHelper.SimpleCallback {
        return object: ItemTouchHelper
        .SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.delete_favorite_title))
                    .setMessage(getString(R.string.sure_delete))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        val character = characterAdapter.getCharacterPosition(viewHolder.adapterPosition)
                        viewModel.delete(character).also {
                            toast(getString(R.string.deleted_character))
                        }
                    }.setNegativeButton(getString(R.string.close_dialog)) { dialog, _ ->
                        dialog.dismiss().also {
                            characterAdapter.notifyDataSetChanged()
                        }
                    }.show()
            }

        }
    }


    private fun configCollectObserver() = lifecycleScope.launch {
        viewModel.favorites.collect { result ->
            when(result) {
                is ResourceState.Success -> {
                    result.data?.let {
                        binding.tvEmptyList.hide()
                        characterAdapter.characters = it.toList()
                    }
                }
                is ResourceState.Empty -> {
                    binding.tvEmptyList.show()
                }
                else -> { }
            }
        }
    }

    private fun configClickAdapter() {
        characterAdapter.setOnClickListener { characterModel ->
            val action = FavoriteCharacterFragmentDirections
                .actionFavoriteCharacterFragmentToDetailsCharacterFragment(characterModel)
            findNavController().navigate(action)
        }
    }

    private fun configRecyclerView() = with(binding) {
        rvFavoriteCharacter.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context)
        }
        ItemTouchHelper(itemTouchHelperCallback()).attachToRecyclerView(rvFavoriteCharacter)
    }

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentFavoriteCharacterBinding = FragmentFavoriteCharacterBinding.inflate(inflater, container, false)
}