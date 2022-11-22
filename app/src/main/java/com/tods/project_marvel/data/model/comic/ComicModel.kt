package com.tods.project_marvel.data.model.comic

import com.google.gson.annotations.SerializedName
import com.tods.project_marvel.data.model.ThumbnailModel
import java.io.Serializable

data class ComicModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thumbnail")
    val thumbnail: ThumbnailModel
): Serializable
