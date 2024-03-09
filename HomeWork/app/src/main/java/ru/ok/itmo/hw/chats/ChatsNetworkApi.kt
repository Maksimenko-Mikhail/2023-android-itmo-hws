package ru.ok.itmo.hw.chats

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import ru.ok.itmo.hw.data_objects.Message

interface ChatsNetworkApi {
    @POST("/logout")
    suspend fun logout(@Header("X-Auth-Token") token : String)

    @GET("/channels")
    suspend fun getChats() : Response<List<String>>

    @GET("/channel/{channelName}")
    suspend fun getChannelMessages(@Path("channelName") channelName : String) : Response<List<Message>>


    @GET("/inbox/{userName}")
    suspend fun getUserInbox(@Path("userName") userName : String,
                             @Header("X-Auth-Token") token : String) : Response<List<Message>>
    companion object {
        fun provideApi(provider : Retrofit) : ChatsNetworkApi {
            return provider.create(ChatsNetworkApi::class.java)
        }
    }
}