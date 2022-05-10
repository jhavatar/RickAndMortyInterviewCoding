package io.chthonic.rickmortychars.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CHARACTER_TABLE_NAME = "character_table"

data class CharacterResults(
    val results: List<CharacterResult>
)

@Entity(tableName = CHARACTER_TABLE_NAME)
data class CharacterResult(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val image: String?,
)