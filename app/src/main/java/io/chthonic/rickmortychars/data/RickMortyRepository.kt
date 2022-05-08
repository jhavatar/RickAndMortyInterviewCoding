package io.chthonic.rickmortychars.data

import dagger.Lazy
import io.chthonic.rickmortychars.data.api.RickMortyApi
import io.chthonic.rickmortychars.domain.model.CharacterInfo
import io.chthonic.rickmortychars.domain.retryIO
import javax.inject.Inject

private const val MAX_RETRY_COUNT = 3

class RickMortyRepository @Inject constructor(private val api: Lazy<RickMortyApi>) {

    suspend fun getCharacters(): List<CharacterInfo> =
        retryIO(times = MAX_RETRY_COUNT) {
            api.get().getCharacters().results
                .mapNotNull {
                    if ((it.id != null) || !it.image.isNullOrEmpty()) {
                        CharacterInfo(
                            id = requireNotNull(it.id),
                            name = it.name ?: "",
                            image = requireNotNull(it.image)
                        )
                    } else null
                }
        }
}