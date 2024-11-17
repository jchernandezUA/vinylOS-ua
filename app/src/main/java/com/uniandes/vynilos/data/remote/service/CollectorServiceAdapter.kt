package com.uniandes.vynilos.data.remote.service

import com.uniandes.vynilos.data.remote.entity.CollectorResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CollectorServiceAdapter {
    @GET("collectors")
    suspend fun getCollectors(): List<CollectorResponse>

    @GET("collectors/{id}")
    suspend fun getCollectorById(@Path("id") id: Int): CollectorResponse
}
