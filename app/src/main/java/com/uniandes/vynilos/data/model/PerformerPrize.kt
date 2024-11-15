@file:Suppress("SpellCheckingInspection")

package com.uniandes.vynilos.data.model

import android.os.Parcelable
import com.uniandes.vynilos.common.convertDateToTimestamp
import com.uniandes.vynilos.data.remote.entity.PerformerPrizeResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class PerformerPrize(
    val id: Int,
    val premiationDate: Long
): Parcelable

fun PerformerPrizeResponse.DTO() = PerformerPrize(
    id = id,
    premiationDate = premiationDate.convertDateToTimestamp()
)

fun List<PerformerPrizeResponse>.DTO() = map { it.DTO() }
