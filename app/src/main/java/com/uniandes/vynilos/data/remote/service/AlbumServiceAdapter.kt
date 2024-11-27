package com.uniandes.vynilos.data.remote.service

import com.uniandes.vynilos.data.remote.entity.AlbumRequest
import com.uniandes.vynilos.data.remote.entity.AlbumResponse
import com.uniandes.vynilos.data.remote.entity.TrackRequest
import com.uniandes.vynilos.data.remote.entity.TracksResponse
import com.uniandes.vynilos.data.remote.entity.ArtistResponse
import com.uniandes.vynilos.data.remote.entity.CommentRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AlbumServiceAdapter {
    @GET("/albums")
    suspend fun getAlbums(): List<AlbumResponse>

    @GET("/albums/{id}")
    suspend fun getAlbumById(@Path("id") id: Int): AlbumResponse

    @POST("/albums")
    suspend fun addAlbum(@Body album: AlbumRequest): AlbumResponse

    @POST("/albums/{id}/tracks")
    suspend fun addTrackToAlbum(
        @Path("id") albumId: Int,
        @Body track: TrackRequest
    ): TracksResponse

    @POST("/albums/{id}/comments")
    suspend fun addComment(@Path("id") id: Int, @Body comment: CommentRequest): AlbumResponse
}
