package com.tods.project_marvel.ui.list

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
class ListCharacterViewModel @Inject constructor (
    private val repository: MarvelRepository
        ): ViewModel() {
    private val _list = MutableStateFlow<ResourceState<CharacterResponseModel>>(ResourceState.Loading())
    val list: StateFlow<ResourceState<CharacterResponseModel>> = _list

    init {
        fetch()
    }

    private fun fetch() = viewModelScope.launch {
        safeFetch()
    }

    private suspend fun safeFetch() {
        try {
            val response = repository.list()
            _list.value = handleResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _list.value = ResourceState.Error("An error with internet connection occurred")
                else -> _list.value = ResourceState.Error("An error converting data occurred")
            }
        }
    }

    private fun handleResponse(response: Response<CharacterResponseModel>): ResourceState<CharacterResponseModel> {
        if (response.isSuccessful) {
            response.body().let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }
}