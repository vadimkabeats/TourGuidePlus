package com.example.tourguideplus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourguideplus.data.network.WeatherResponse
import com.example.tourguideplus.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Состояния UI
sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val data: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel : ViewModel() {
    private val repo = WeatherRepository()

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    private var currentCity: String = "Москва"

    init {
        fetch(currentCity)
    }

    fun fetch(city: String) {
        currentCity = city
        _uiState.value = WeatherUiState.Loading

        viewModelScope.launch {
            // repo.fetchWeather возвращает kotlin.Result<WeatherResponse>
            val result = repo.fetchWeather(city)
            if (result.isSuccess) {
                result.getOrNull()?.let { data ->
                    _uiState.value = WeatherUiState.Success(data)
                } ?: run {
                    _uiState.value = WeatherUiState.Error("Пустой ответ от сервера")
                }
            } else {
                val err = result.exceptionOrNull()?.localizedMessage ?: "Неизвестная ошибка"
                _uiState.value = WeatherUiState.Error(err)
            }
        }
    }

    fun refresh() = fetch(currentCity)
}