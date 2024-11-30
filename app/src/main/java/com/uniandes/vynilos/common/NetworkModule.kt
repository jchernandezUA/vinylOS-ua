package com.uniandes.vynilos.common

import com.uniandes.vynilos.data.remote.service.AlbumServiceAdapter
import com.uniandes.vynilos.data.remote.service.ArtistServiceAdapter
import com.uniandes.vynilos.data.remote.service.CollectorServiceAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://back-vinylos-e11-da7f5dfc1c26.herokuapp.com/"
    //private const val BASE_URL = "http://web:3000/"
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val albumServiceAdapter: AlbumServiceAdapter = getRetrofit().create(AlbumServiceAdapter::class.java)
    val artistServiceAdapter: ArtistServiceAdapter = getRetrofit().create(ArtistServiceAdapter::class.java)
    val collectorServiceAdapter : CollectorServiceAdapter =  getRetrofit().create(CollectorServiceAdapter::class.java)

    fun <T> createService(serviceClass: Class<T>): T = getRetrofit().create(serviceClass)
}