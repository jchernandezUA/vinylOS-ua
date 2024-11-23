package com.uniandes.vynilos.di

import com.uniandes.vynilos.common.NetworkModule.createService
import com.uniandes.vynilos.data.model.Artist
import com.uniandes.vynilos.data.remote.service.AlbumServiceAdapter
import com.uniandes.vynilos.data.remote.service.ArtistServiceAdapter
import com.uniandes.vynilos.data.remote.service.CollectorServiceAdapter
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
    viewModel { ListAlbumViewModel(get()) }
    viewModel { AlbumViewModel(get(), get()) }
    viewModel { AddAlbumViewModel(get()) }
}

val artistModule = module {
    single<ArtistRepository> { mockk() } // Usa el mock proporcionado en las pruebas
    factory { ListArtistViewModel(get()) }
    viewModel { (artist: Artist) -> ArtistViewModel(artist, get()) }
}

val collectorModule = module {
    single<CollectorRepository> { mockk() } // Usa el mock proporcionado en las pruebas
    viewModel { ListCollectorViewModel(get()) }
    viewModel { (collectorId: Int) -> CollectorDetailViewModel(collectorId, get()) }
}
