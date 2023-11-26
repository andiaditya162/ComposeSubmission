package com.dityapra.compose.di

import com.dityapra.compose.data.HeroRepository

object Injection {
    fun provideRepository(): HeroRepository {
        return HeroRepository.getInstance()
    }
}