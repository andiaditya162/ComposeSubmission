package com.dityapra.compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dityapra.compose.data.HeroRepository
import com.dityapra.compose.model.Hero
import com.dityapra.compose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: HeroRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Hero>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Hero>>
        get() = _uiState

    fun getHeroById(id: Int) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        _uiState.value = UiState.Success(repository.getHeroById(id))
    }


    fun updateHero(id: Int, newState: Boolean) = viewModelScope.launch {
        repository.updateHero(id, !newState)
            .collect { isUpdated ->
                if (isUpdated) getHeroById(id)
            }
    }
}