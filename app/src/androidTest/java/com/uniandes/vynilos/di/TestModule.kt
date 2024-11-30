package com.uniandes.vynilos.di

import com.uniandes.vynilos.data.model.Album
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.repository.AlbumRepository
import com.uniandes.vynilos.data.repository.ArtistRepository
import com.uniandes.vynilos.data.repository.CollectorRepository
import com.uniandes.vynilos.presentation.viewModel.ArtistViewModel
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel
import com.uniandes.vynilos.presentation.viewModel.ListArtistViewModel
import com.uniandes.vynilos.presentation.viewModel.ListCollectorViewModel
import com.uniandes.vynilos.presentation.viewModel.album.AddAlbumViewModel
import com.uniandes.vynilos.presentation.viewModel.album.AlbumViewModel
import com.uniandes.vynilos.presentation.viewModel.album.ListAlbumViewModel
import io.mockk.mockk
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val albumModule = module {
    single<AlbumRepository> { mockk() }
    viewModel { (album: Album) ->
        AlbumViewModel(
            album = album,
            albumRepository = get()
        )
    }
    viewModel { ListAlbumViewModel(get()) }
    viewModel { AddAlbumViewModel(get()) }
}

val artistModule = module {
    single<ArtistRepository> { mockk() }
    factory { ListArtistViewModel(get()) }
    viewModel { (artist: Artist) -> ArtistViewModel(artist, get()) }
}

val collectorModule = module {
    single<CollectorRepository> { mockk() }
    viewModel { ListCollectorViewModel(get()) }
    //viewModel { (collectorId: Int) -> CollectorDetailViewModel(collectorId, get()) }
}
