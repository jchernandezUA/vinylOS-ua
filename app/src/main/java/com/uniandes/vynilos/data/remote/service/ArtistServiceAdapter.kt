package com.uniandes.vynilos.data.remote.service

import com.uniandes.vynilos.data.remote.entity.ArtistResponse
import retrofit2.http.GET

interface ArtistServiceAdapter {
    @GET("/bands")
    suspend fun getArtist(): List<ArtistResponse>
}