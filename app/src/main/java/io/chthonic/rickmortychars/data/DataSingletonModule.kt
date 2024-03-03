package io.chthonic.rickmortychars.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chthonic.rickmortychars.data.rest.RickMortyApi
import io.chthonic.rickmortychars.data.database.CharactersDao
import io.chthonic.rickmortychars.data.database.DatabaseFactory
import io.chthonic.rickmortychars.data.database.RickMortyDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://rickandmortyapi.com/api/"

@Module
@InstallIn(SingletonComponent::class)
class DataSingletonModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor(
                logger = HttpLoggingInterceptor.Logger.DEFAULT
            ).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideRickMortyApi(retrofit: Retrofit): RickMortyApi =
        retrofit.create(RickMortyApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(context: Context): RickMortyDatabase =
        DatabaseFactory.createDatabase(context)

    @Provides
    fun provideCharactersDao(db: RickMortyDatabase): CharactersDao =
        db.charactersDao()
}