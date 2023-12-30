package com.acclorite.books_history.data.di

import com.acclorite.books_history.data.use_case.ChangeLanguageImpl
import com.acclorite.books_history.domain.use_case.ChangeLanguage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindChangeLanguage(
        changeLanguageImpl: ChangeLanguageImpl
    ): ChangeLanguage
}