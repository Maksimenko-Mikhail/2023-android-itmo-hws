package ru.ok.itmo.HomeWork

sealed class TimerUiState {
    data class Data(val timeExecution: Int) : TimerUiState()
}