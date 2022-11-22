package com.tods.project_marvel.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tods.project_marvel.data.model.ThumbnailModel

class MarvelConverters {

    @TypeConverter
    fun fromThumbnail(thumbnail: ThumbnailModel): String = Gson().toJson(thumbnail)

    @TypeConverter
    fun toThumbnail(thumbnail: String): ThumbnailModel = Gson().fromJson(thumbnail, ThumbnailModel::class.java)
}