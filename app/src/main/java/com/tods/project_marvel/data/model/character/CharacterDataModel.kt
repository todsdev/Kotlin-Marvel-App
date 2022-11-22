package com.tods.project_marvel.data.model.character

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CharacterDataModel(
    @SerializedName("results")
    val results: List<CharacterModel>
): Serializable
