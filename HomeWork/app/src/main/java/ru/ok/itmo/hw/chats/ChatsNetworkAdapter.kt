package ru.ok.itmo.hw.chats

import android.util.Log
import ru.ok.itmo.hw.data_objects.Message
import ru.ok.itmo.hw.network.NetworkProvider

class ChatsNetworkAdapter {
    private val api = ChatsNetworkApi.provideApi(NetworkProvider.retrofit())
    suspend fun logout(token : String) : Boolean {
        return try {
            api.logout(token)
            return true
        } catch (e : Exception) {
            false
        }
    }

    suspend fun getAllChats() : Result<List<String>> {
        return try {
            val response = api.getChats()
            Result.success(response.body()!!)
        } catch (e : Exception) {
            Result.failure(Exception("Bad internet connection"))
        }

    }

    suspend fun getChatsMessages(channelName : String) : Result<List<Message>> {
        return try {
            Log.v("channelName", channelName)
            val response = api.getChannelMessages(channelName)
            Result.success(response.body()!!)
        } catch (e : Exception) {
            Result.failure(Exception("Bad internet connection"))
        }
    }

}