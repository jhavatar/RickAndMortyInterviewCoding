package io.chthonic.rickmortychars.data.rickmorty.rest.models


data class CharacterResults(
    val results: List<CharacterResult>
)

data class CharacterResult(
    val id: Int?,
    val name: String?,
    val image: String?,
)