package io.chthonic.rickmortychars.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chthonic.rickmortychars.domain.presentationapi.GetCharacterListUseCase
import io.chthonic.rickmortychars.domain.presentationapi.GetCharacterUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DomainModule {

    @Provides
    @Singleton
    fun provideGetCharacterListUseCase(impl: GetCharacterListUseCaseImpl): GetCharacterListUseCase =
        impl

    @Provides
    @Singleton
    fun provideGetCharacterUseCase(impl: GetCharacterUseCaseImpl): GetCharacterUseCase = impl
}