package io.chthonic.rickmortychars.data

import androidx.paging.*
import io.chthonic.rickmortychars.domain.model.CharacterInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RickMortyRepository @Inject constructor(
    private val characterResultPagingSource: CharacterResultPagingSource
) {

    fun getCharacters(): Flow<PagingData<CharacterInfo>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { characterResultPagingSource }
        ).flow.map { pagingData ->
            pagingData.filter {
                it.id != null && !it.image.isNullOrEmpty()
            }.map {
                CharacterInfo(
                    id = requireNotNull(it.id),
                    name = it.name ?: "",
                    image = requireNotNull(it.image)
                )
            }
        }
}