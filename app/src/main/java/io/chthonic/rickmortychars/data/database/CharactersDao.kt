package io.chthonic.rickmortychars.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.chthonic.rickmortychars.data.models.CHARACTER_TABLE_NAME
import io.chthonic.rickmortychars.data.models.CharacterResult


@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<CharacterResult>)

    @Query("SELECT * FROM $CHARACTER_TABLE_NAME ORDER BY id ASC")
    fun pagingSource(): PagingSource<Int, CharacterResult>

    @Query("DELETE FROM $CHARACTER_TABLE_NAME")
    suspend fun clearAll()

    @Query("SELECT COUNT(id) FROM $CHARACTER_TABLE_NAME")
    suspend fun getCharacterCount(): Int

    @Query("SELECT * FROM $CHARACTER_TABLE_NAME WHERE id = :id")
    suspend fun getCharacter(id: Int): CharacterResult?
}