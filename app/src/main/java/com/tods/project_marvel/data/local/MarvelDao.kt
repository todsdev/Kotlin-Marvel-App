package com.tods.project_marvel.data.local

import androidx.room.*
import com.tods.project_marvel.data.model.character.CharacterModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MarvelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characterModel: CharacterModel): Long

    @Delete
    suspend fun delete(characterModel: CharacterModel)

    @Query("SELECT * FROM characterModel ORDER BY id")
    fun getAll(): Flow<List<CharacterModel>>
}