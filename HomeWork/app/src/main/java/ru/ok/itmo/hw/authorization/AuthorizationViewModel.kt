package ru.ok.itmo.hw.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//@HiltViewModel
class AuthorizationViewModel(): ViewModel() {

    private val authorizationAdapter = AuthorizationAdapter()


    private val _uiStateLiveData = MutableLiveData<LoginUiState>()
    val uiStateLiveData: LiveData<LoginUiState>
        get() = _uiStateLiveData
    private var token : String? = null
    fun login(login : String, password : String) {
        viewModelScope.launch {
            authorizationAdapter.login(login, password).onSuccess {
                token = it
                _uiStateLiveData.value = LoginUiState.Data(token!!)
            }.onFailure {
                _uiStateLiveData.value = LoginUiState.Error(it)
            }
        }
    }
    
}