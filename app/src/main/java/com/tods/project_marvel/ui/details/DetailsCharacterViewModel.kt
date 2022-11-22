package com.tods.project_marvel.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tods.project_marvel.data.model.character.CharacterModel
import com.tods.project_marvel.data.model.character.CharacterResponseModel
import com.tods.project_marvel.data.model.comic.ComicResponseModel
import com.tods.project_marvel.repository.MarvelRepository
import com.tods.project_marvel.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailsCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
    ): ViewModel() {

    private val _details = MutableStateFlow<ResourceState<ComicResponseModel>>(ResourceState.Loading())
    val details: StateFlow<ResourceState<ComicResponseModel>> = _details

    fun fetch(characterId: Int) = viewModelScope.launch {
        safeFetch(characterId)
    }

    private suspend fun safeFetch(characterId: Int) {
        _details.value = ResourceState.Loading()
        try {
            val response = repository.getComics(characterId)
            _details.value = handleResponse(response)
        } catch (t: Throwable) {
            when(t) {
                is IOException -> _details.value = ResourceState.Error("An error with internet connection happened")
                else -> _details.value = ResourceState.Error("An error converting data occurred")
            }
        }
    }

    private fun handleResponse(response: Response<ComicResponseModel>): ResourceState<ComicResponseModel> {
        if(response.isSuccessful) {
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }

    fun insert(characterModel: CharacterModel) = viewModelScope.launch {
        repository.insert(characterModel)
    }
}