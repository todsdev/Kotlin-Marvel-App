package com.tods.project_marvel.data.remote

import com.tods.project_marvel.data.model.character.CharacterResponseModel
import com.tods.project_marvel.data.model.comic.ComicResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {

    @GET("characters")
    suspend fun list(
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): Response<CharacterResponseModel>

    @GET("characters/{characterId}/comics")
    suspend fun getComics(
        @Path(
            value = "characterId",
            encoded = true
        ) characterId: Int
    ): Response<ComicResponseModel>
}