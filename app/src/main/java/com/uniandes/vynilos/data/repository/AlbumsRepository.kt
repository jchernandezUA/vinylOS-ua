package com.uniandes.vynilos.data.repository

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.resultOrError
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.remote.service.AlbumServiceAdapter

interface AlbumRepository {
    suspend fun getAlbums(): DataState<List<Album>>
    suspend fun getAlbum(id: Int): DataState<Album>
}
class AlbumRepositoryImpl(
    private val albumService: AlbumServiceAdapter
): AlbumRepository {

    override suspend fun getAlbums(): DataState<List<Album>> {
        return resultOrError {
            val userResponse = albumService.getAlbums()
            userResponse.DTO()
        }
    }

    override suspend fun getAlbum(id: Int): DataState<Album> {
        return resultOrError {
            val albumResponse = albumService.getAlbumById(id)
            albumResponse.DTO()
        }
    }
}