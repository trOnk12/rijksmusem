package com.example.maxdoro_recruitment_task.domain.entity

data class ArtDetails(
    val artDetailsIdentifier: ArtDetailsIdentifier,
    val artDetailsDescriptionInfo: ArtDetailsDescriptionInfo,
)

data class ArtDetailsDescriptionInfo(
    val title: String,
    val alternativeTitles: List<String>,
    val description: String,
    val webImageUrl: String,
)

data class ArtDetailsIdentifier(
    val objectNumber: String,
)