package ru.ok.itmo.hw.authorization

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import ru.ok.itmo.hw.data_objects.LoginRequestBody


interface AuthorizationApi {
    @POST("/login")
    suspend fun login(@Body loginRequestBody: LoginRequestBody) : Response<String>

    @POST("/addusr")
    suspend fun register(@Body login : String) : Response<String>



    companion object {
        fun provideApi(provider : Retrofit) : AuthorizationApi {
            return provider.create(AuthorizationApi::class.java)
        }
    }
}