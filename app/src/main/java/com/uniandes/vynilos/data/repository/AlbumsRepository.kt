package com.uniandes.vynilos.data.repository

import com.uniandes.vynilos.common.DataState
import com.uniandes.vynilos.common.resultOrError
import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.model.Tracks
import com.uniandes.vynilos.data.model.toDomain
import com.uniandes.vynilos.data.remote.entity.TrackRequest
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.model.Comment
import com.uniandes.vynilos.data.model.DTO
import com.uniandes.vynilos.data.model.toDomain
import com.uniandes.vynilos.data.remote.service.AlbumServiceAdapter

interface AlbumRepository {
    suspend fun getAlbums(): DataState<List<Album>>
    suspend fun getAlbum(id: Int): DataState<Album>
    suspend fun addAlbum(album: Album): DataState<Album>
    suspend fun addTrackToAlbum(albumId: Int, track: Tracks): DataState<Tracks>
    suspend fun addComment(albumId: Int, comment: Comment): DataState<Unit>
}
class AlbumRepositoryImpl(
    private val albumService: AlbumServiceAdapter
) : AlbumRepository {

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

    override suspend fun addAlbum(album: Album): DataState<Album> {
        return resultOrError {
            val albumRequest = album.toDomain()
            val albumResponse = albumService.addAlbum(albumRequest)
            albumResponse.DTO()
        }
    }

    override suspend fun addTrackToAlbum(albumId: Int, track: Tracks): DataState<Tracks> {
        return resultOrError {
            val request = TrackRequest(
                name = track.name,
                duration = track.duration
            )
            val response = albumService.addTrackToAlbum(albumId, request)
            response.DTO()
        }
    }

    override suspend fun addComment(albumId: Int, comment: Comment): DataState<Unit> {
        return resultOrError {
            val commentRequest = comment.toDomain()
            albumService.addComment(albumId, commentRequest)
            Unit

        }
    }
}
