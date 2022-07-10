package io.chthonic.rickmortychars.data.api

import io.chthonic.rickmortychars.data.models.CharacterResults
import retrofit2.http.GET
import retrofit2.http.Query

interface RickMortyApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int = 1): CharacterResults
}