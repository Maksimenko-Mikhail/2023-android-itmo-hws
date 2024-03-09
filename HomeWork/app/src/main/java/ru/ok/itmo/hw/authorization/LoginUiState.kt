package ru.ok.itmo.hw.authorization

sealed class LoginUiState {
    data class Data(val result: String) : LoginUiState()
    data class Error(val throwable: Throwable) : LoginUiState()
    data object NoState: LoginUiState()
}