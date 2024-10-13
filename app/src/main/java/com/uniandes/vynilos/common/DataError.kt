package com.uniandes.vynilos.common

data class DataError(
    override val message: String,
    val statusCode: Int? = null
): Exception()
