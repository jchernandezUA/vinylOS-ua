package com.uniandes.vynilos.data.repository

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.resultOrError
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.remote.service.AlbumServiceAdapter

interface AlbumsRepository {
    suspend fun getAlbums(): DataState<List<Album>>
}
class AlbumsRepositoryImpl(
    private val albumService: AlbumServiceAdapter
): AlbumsRepository {

    override suspend fun getAlbums(): DataState<List<Album>> {
        return resultOrError {
            val userResponse = albumService.getAlbums()
            userResponse.DTO()
        }
    }
}