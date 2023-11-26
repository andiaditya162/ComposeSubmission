package com.dityapra.compose.data

import com.dityapra.compose.model.Hero
import com.dityapra.compose.model.HeroData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class HeroRepository {
    private val dummyHero = mutableListOf<Hero>()

    init {
        if (dummyHero.isEmpty()) {
            HeroData.dummyHero.forEach {
                dummyHero.add(it)
            }
        }
    }

    fun getHeroById(heroId: Int): Hero {
        return dummyHero.first {
            it.id == heroId
        }
    }

    fun getFavoriteHero(): Flow<List<Hero>> {
        return flowOf(dummyHero.filter { it.isFavorite })
    }

    fun searchHero(query: String) = flow {
        val data = dummyHero.filter {
            it.name.contains(query, ignoreCase = true)
        }
        emit(data)
    }

    fun updateHero(heroId: Int, newState: Boolean): Flow<Boolean> {
        val index = dummyHero.indexOfFirst { it.id == heroId }
        val result = if (index >= 0) {
            val hero = dummyHero[index]
            dummyHero[index] = hero.copy(isFavorite = newState)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    companion object {
        @Volatile
        private var instance: HeroRepository? = null

        fun getInstance(): HeroRepository =
            instance ?: synchronized(this) {
                HeroRepository().apply {
                    instance = this
                }
            }
    }
}