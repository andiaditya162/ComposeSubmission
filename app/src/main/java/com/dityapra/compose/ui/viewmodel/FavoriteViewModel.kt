package com.dityapra.compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dityapra.compose.data.HeroRepository
import com.dityapra.compose.model.Hero
import com.dityapra.compose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: HeroRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Hero>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Hero>>>
        get() = _uiState

    fun getFavoriteHero() = viewModelScope.launch {
        repository.getFavoriteHero()
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect {
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateHero(id: Int, newState: Boolean) {
        repository.updateHero(id, newState)
        getFavoriteHero()
    }
}