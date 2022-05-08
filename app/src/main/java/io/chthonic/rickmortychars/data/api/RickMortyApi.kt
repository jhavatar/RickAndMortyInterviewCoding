package io.chthonic.rickmortychars.data.api

import io.chthonic.rickmortychars.data.model.CharacterResults
import retrofit2.http.GET
import retrofit2.http.Query

interface RickMortyApi {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int = 1): CharacterResults
}