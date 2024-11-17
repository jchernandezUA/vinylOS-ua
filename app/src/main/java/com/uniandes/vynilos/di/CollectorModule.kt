package com.uniandes.vynilos.di

import com.uniandes.vynilos.common.NetworkModule.createService
import com.uniandes.vynilos.data.remote.service.CollectorServiceAdapter
import com.uniandes.vynilos.data.repository.CollectorRepository
import com.uniandes.vynilos.data.repository.CollectorRepositoryImpl
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel
import com.uniandes.vynilos.presentation.viewModel.ListCollectorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val collectorModule = module {
    single { createService(CollectorServiceAdapter::class.java) }
    single<CollectorRepository> { CollectorRepositoryImpl(get()) }
    viewModel { ListCollectorViewModel(get()) }
    viewModel { (collectorId: Int) -> CollectorDetailViewModel(collectorId, get()) }
}
