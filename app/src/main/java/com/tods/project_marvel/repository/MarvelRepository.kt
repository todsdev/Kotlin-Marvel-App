package com.tods.project_marvel.repository

import com.tods.project_marvel.data.local.MarvelDao
import com.tods.project_marvel.data.model.character.CharacterModel
import com.tods.project_marvel.data.remote.ServiceApi
import javax.inject.Inject

class MarvelRepository @Inject constructor(
    private val api: ServiceApi,
    private val dao: MarvelDao
) {
    suspend fun list(nameStartsWith: String? = null) = api.list(nameStartsWith)
    suspend fun getComics(characterId: Int) = api.getComics(characterId)
    suspend fun insert(characterModel: CharacterModel) = dao.insert(characterModel)
    suspend fun delete(characterModel: CharacterModel) = dao.delete(characterModel)
    fun getAll() = dao.getAll()
}