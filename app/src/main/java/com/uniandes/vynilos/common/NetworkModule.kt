package com.uniandes.vynilos.common

import com.uniandes.vynilos.data.remote.service.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val userService: UserService = getRetrofit().create(UserService::class.java)
}