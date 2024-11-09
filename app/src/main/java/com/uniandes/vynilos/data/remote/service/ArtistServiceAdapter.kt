package com.uniandes.vynilos.data.remote.service

import com.uniandes.vynilos.data.remote.entity.ArtistResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtistServiceAdapter {
    @GET("/bands")
    suspend fun getArtist(): List<ArtistResponse>
    @GET("/bands/{id}")
    suspend fun getArtistById(@Path("id") id: Int): ArtistResponse
}