package ru.ok.itmo.hw.chats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ok.itmo.hw.data_objects.ChatItem

//@HiltViewModel
class ChatsViewModel constructor(private val token : String): ViewModel() {

    private val chatsNetworkAdapter = ChatsNetworkAdapter()
    private val _uiStateLiveData = MutableLiveData<ChatsUiState>(ChatsUiState.NoState)
    val uiStateLiveData: LiveData<ChatsUiState>
        get() = _uiStateLiveData
    fun logout() {

        viewModelScope.launch(Dispatchers.IO) {
            chatsNetworkAdapter.logout(token)
        }
    }

    fun getAllChatItems() {
        _uiStateLiveData.value = ChatsUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            chatsNetworkAdapter.getAllChats().onSuccess {
                val res = getChannelsInfo(it)
                viewModelScope.launch(Dispatchers.Main) {
                    _uiStateLiveData.value = if (res.isSuccess) ChatsUiState.Success(res.getOrNull()!!)
                    else ChatsUiState.Error(res.exceptionOrNull()!!)
                }
            }.onFailure {
                viewModelScope.launch(Dispatchers.Main) {
                    _uiStateLiveData.value = ChatsUiState.Error(it)
                }
            }
        }
    }


    private suspend fun getChannelsInfo(chats : List<String>) : Result<MutableList<ChatItem>> {
        val result = ArrayList<ChatItem>()
        chats.forEach {
            name -> chatsNetworkAdapter.getChatsMessages(name).onSuccess {
                Log.v("LAST MESSAGE", it.last().toString())
                result.add(ChatItem(
                    name,
                    it.last()
                ))
            }.onFailure {
                return Result.failure(it)
            }
        }
        return Result.success(result)
    }
    @Suppress("UNCHECKED_CAST")
    class Factory internal constructor(
        private val token: String
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatsViewModel(token) as T
        }
    }
}