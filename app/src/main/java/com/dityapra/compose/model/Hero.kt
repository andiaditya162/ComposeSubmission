package com.dityapra.compose.model

data class Hero(
    val id: Int,
    val name: String,
    val role: String,
    val image: Int,
    val description: String,
    val lane: String,
    val rate: Double,
    val active: String,
    var isFavorite: Boolean = false
)