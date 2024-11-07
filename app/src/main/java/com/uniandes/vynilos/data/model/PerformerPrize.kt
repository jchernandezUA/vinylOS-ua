package com.uniandes.vynilos.data.model

import com.uniandes.vynilos.data.remote.entity.PerformerPrizeResponse

data class PerformerPrize(
    val id: Int,
    val premiationDate: String
)
fun PerformerPrizeResponse.DTO() = PerformerPrize(
    id = id,
    premiationDate = premiationDate
)
fun List<PerformerPrizeResponse>.DTO() = map { it.DTO() }
