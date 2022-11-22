package com.tods.project_marvel.ui.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tods.project_marvel.R
import com.tods.project_marvel.data.model.character.CharacterModel
import com.tods.project_marvel.databinding.FragmentDetailsCharacterBinding
import com.tods.project_marvel.state.ResourceState
import com.tods.project_marvel.ui.adapters.ComicAdapter
import com.tods.project_marvel.ui.base.BaseFragment
import com.tods.project_marvel.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DetailsCharacterFragment: BaseFragment<FragmentDetailsCharacterBinding, DetailsCharacterViewModel>() {
    override val viewModel: DetailsCharacterViewModel by viewModels()
    private val args: DetailsCharacterFragmentArgs by navArgs()
    private val comicAdapter by lazy { ComicAdapter() }
    private lateinit var characterModel: CharacterModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configInitialSettings()
        configRecyclerView()
        configLoadedCharacter(characterModel)
        configCollectObserver()
        configCharacterDetailsOnClickListener()
    }

    private fun configCharacterDetailsOnClickListener() {
        binding.tvDescriptionCharacterDetails.setOnClickListener {
            configDialog(characterModel)
        }
    }

    private fun configDialog(characterModel: CharacterModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(characterModel.name)
            .setMessage(characterModel.description)
            .setNegativeButton(getString(R.string.close_dialog)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite -> {
                viewModel.insert(characterModel)
                toast(getString(R.string.favorite_successfully))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configInitialSettings() {
        characterModel = args.character
        viewModel.fetch(characterModel.id)
    }

    private fun configCollectObserver() = lifecycleScope.launch {
        viewModel.details.collect { result ->
            when(result) {
                is ResourceState.Success -> {
                    binding.progressBarDetail.hide()
                    result.data?.let { values ->
                        if(values.data.result.isNotEmpty()) {
                            comicAdapter.comics = values.data.result.toList()
                        } else {
                            toast(getString(R.string.empty_comic_list))
                        }
                    }
                }
                is ResourceState.Error -> {
                    binding.progressBarDetail.hide()
                    result.message?.let { message ->
                        toast(getString(R.string.an_error_occurred))
                        Timber.tag("DetailsCharacterFragment").e("Error: $message")
                    }
                }
                is ResourceState.Loading -> {
                    binding.progressBarDetail.show()
                }
                 else -> { }
            }
        }
    }

    private fun configLoadedCharacter(characterModel: CharacterModel) = with(binding) {
        tvNameCharacterDetails.text = characterModel.name
        if (characterModel.description.isEmpty()) {
            tvDescriptionCharacterDetails.text = requireContext().getString(R.string.empty_description)
        } else {
            tvDescriptionCharacterDetails.text = characterModel.description.limitedDescription(100)
        }
        loadImage(imgCharacterDetails, characterModel.thumbnail.path, characterModel.thumbnail.extension)

    }

    private fun configRecyclerView() = with(binding) {
        rvComics.apply {
            adapter = comicAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun recoverViewBinding(inflater: LayoutInflater, container: ViewGroup?):
            FragmentDetailsCharacterBinding = FragmentDetailsCharacterBinding.inflate(inflater, container, false)
}