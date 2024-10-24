package com.uniandes.vynilos.data.remote.service

import com.uniandes.vynilos.data.remote.entity.AlbumResponse
import retrofit2.http.GET

interface AlbumServiceAdapter {
    @GET("/albums")
    suspend fun getAlbums(): List<AlbumResponse>
}