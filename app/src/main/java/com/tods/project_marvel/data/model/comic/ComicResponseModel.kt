package com.tods.project_marvel.data.model.comic

import com.google.gson.annotations.SerializedName

data class ComicResponseModel(
    @SerializedName("data")
    val data: ComicDataModel
)
