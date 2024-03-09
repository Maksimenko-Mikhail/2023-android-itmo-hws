package ru.ok.itmo.hw.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object NetworkProvider {
//    private fun interceptor() = Interceptor { chain ->
//        val request: Request =
//            chain.request()
//                .newBuilder()
//                .build()
//        chain.proceed(request)
//    }
//
//    private fun client() = OkHttpClient.Builder()
//        .addInterceptor(interceptor())
//        .build()

    fun retrofit() : Retrofit {


        return Retrofit.Builder()
            .baseUrl("https://faerytea.name:8008")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
//                    .serializeNulls()
//                    .serializeSpecialFloatingPointValues()
                    .setLenient()
                    .create()))
//            .client(client())
            .build()
    }
}