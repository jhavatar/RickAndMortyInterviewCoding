package io.chthonic.rickmortychars.data.rickmorty

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chthonic.rickmortychars.data.rickmorty.rest.RickMortyRestApi
import io.chthonic.rickmortychars.data.rickmorty.database.CharactersDao
import io.chthonic.rickmortychars.data.rickmorty.database.DatabaseFactory
import io.chthonic.rickmortychars.data.rickmorty.database.RickMortyDatabase
import io.chthonic.rickmortychars.domain.dataapi.RickMortyRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://rickandmortyapi.com/api/"

@Module
@InstallIn(SingletonComponent::class)
internal class RickMortySingletonModule {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideRickMortyApi(retrofit: Retrofit): RickMortyRestApi =
        retrofit.create(RickMortyRestApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(context: Context): RickMortyDatabase =
        DatabaseFactory.createDatabase(context)

    @Provides
    fun provideCharactersDao(db: RickMortyDatabase): CharactersDao =
        db.charactersDao()

    @Provides
    fun provideRickMortyRepository(impl: RickMortyRepositoryImpl): RickMortyRepository =
        impl
}