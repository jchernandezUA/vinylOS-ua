package com.uniandes.vynilos.data.remote.service

import com.uniandes.vynilos.data.remote.entity.AlbumResponse
import com.uniandes.vynilos.data.remote.entity.ArtistResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumServiceAdapter {
    @GET("/albums")
    suspend fun getAlbums(): List<AlbumResponse>
    @GET("/bands/{id}")
    suspend fun getAlbumById(@Path("id") id: Int): AlbumResponse
}