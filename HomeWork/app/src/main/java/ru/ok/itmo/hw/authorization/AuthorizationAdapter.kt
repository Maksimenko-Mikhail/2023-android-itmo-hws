package ru.ok.itmo.hw.authorization

import ru.ok.itmo.hw.data_objects.LoginRequestBody
import ru.ok.itmo.hw.network.NetworkProvider

class AuthorizationAdapter {
    private val api = AuthorizationApi.provideApi(NetworkProvider.retrofit())
    suspend fun login(login: String, password : String) : Result<String?> {
        return try {
            val response = api.login(LoginRequestBody(login, password))
            when (response.code()) {
                200 -> Result.success(response.body())
                401 -> Result.failure(Exception("q"))
                else  -> Result.failure(Exception(response.code().toString()))
            }
        } catch (e : Exception) {
            Result.failure(e)
        }
    }




}