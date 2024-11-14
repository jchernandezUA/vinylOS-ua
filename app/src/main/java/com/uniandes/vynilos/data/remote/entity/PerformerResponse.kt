package com.uniandes.vynilos.data.remote.entity

data class PerformerResponse(
    val id: Int,
    val name: String,
    val image: String?,
    val description: String,
    val birthDate: String?,
    val creationDate: String?,
    val collectors: List<CollectorBasicInfoResponse> = emptyList()
)

data class CollectorBasicInfoResponse(
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String
)
