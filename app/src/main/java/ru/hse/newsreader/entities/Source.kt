package ru.hse.newsreader.entities

data class Source(
    val location: String? = null,
    val temperature: String? = null,
    val pressure: String? = null,
    val weather: String? = null,

    val wind: String? = null,
    val seaLevel: String? = null
)