package com.uniandes.vynilos.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniandes.vynilos.common.NetworkModule
import com.uniandes.vynilos.common.getSafeParcelableExtra
import com.uniandes.vynilos.data.model.Collector
import com.uniandes.vynilos.data.repository.CollectorRepositoryImpl
import com.uniandes.vynilos.presentation.navigation.CollectorNavigation
import com.uniandes.vynilos.presentation.viewModel.CollectorDetailViewModel

class CollectorActivity : ComponentActivity()  {

    companion object {
        const val COLLECTOR = "collector"
    }

    private lateinit var collectorViewModel : CollectorDetailViewModel
    private val collectorRepository = CollectorRepositoryImpl(NetworkModule.collectorServiceAdapter)

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val collector = intent.getSafeParcelableExtra<Collector>(COLLECTOR)
        if (collector == null) finish()

        collectorViewModel = CollectorDetailViewModel(collector!!, collectorRepository)
        setContent{
            CollectorNavigation(collectorViewModel) {
                finish()
            }
        }

    }
}