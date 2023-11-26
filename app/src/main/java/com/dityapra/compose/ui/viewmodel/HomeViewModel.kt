package com.dityapra.compose.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dityapra.compose.data.HeroRepository
import com.dityapra.compose.model.Hero
import com.dityapra.compose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HeroRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Hero>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Hero>>>
        get() = _uiState

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) = viewModelScope.launch {
        _query.value = newQuery
        repository.searchHero(_query.value)
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect {
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateHero(id: Int, newState: Boolean) = viewModelScope.launch {
        repository.updateHero(id, newState)
            .collect { isUpdated ->
                if (isUpdated) search(_query.value)
            }
    }
}