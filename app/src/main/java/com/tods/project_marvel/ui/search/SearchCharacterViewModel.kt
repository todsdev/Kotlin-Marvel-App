package com.tods.project_marvel.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tods.project_marvel.data.model.character.CharacterResponseModel
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
class SearchCharacterViewModel @Inject constructor (
    private val repository: MarvelRepository
        ): ViewModel() {

    private val _searchCharacter = MutableStateFlow<ResourceState<CharacterResponseModel>>(ResourceState.Empty())
    val searchCharacter: StateFlow<ResourceState<CharacterResponseModel>> = _searchCharacter

    fun fetch(nameStartsWith: String) = viewModelScope.launch {
        safeFetch(nameStartsWith)
    }

    private suspend fun safeFetch(nameStartsWith: String) {
        _searchCharacter.value = ResourceState.Loading()
        try {
            val response = repository.list(nameStartsWith)
            _searchCharacter.value = handleResponse(response)
        } catch (t: Throwable) {
            when(t) {
                is IOException -> _searchCharacter.value = ResourceState.Error("An error with internet connection occurred")
                else -> _searchCharacter.value = ResourceState.Error("An error converting data occurred")
            }
        }
    }

    private fun handleResponse(response: Response<CharacterResponseModel>): ResourceState<CharacterResponseModel> {
        if (response.isSuccessful) {
            response.body().let { values ->
                return ResourceState.Success(values)
            }
        } else {
            return ResourceState.Error(response.message())
        }
    }


}