package com.uniandes.vynilos.remote.service

import com.uniandes.vynilos.remote.entity.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/users/{id}")
    suspend fun getUser(@Path("id") id: String): UserResponse
}