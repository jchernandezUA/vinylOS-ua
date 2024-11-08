package com.uniandes.vynilos.data.model

import com.uniandes.vynilos.data.remote.entity.PerformerPrizeResponse
import java.io.Serializable

data class PerformerPrize(
    val id: Int,
    val premiationDate: String
): Serializable

fun PerformerPrizeResponse.DTO() = PerformerPrize(
    id = id,
    premiationDate = premiationDate
)

fun List<PerformerPrizeResponse>.DTO() = map { it.DTO() }
