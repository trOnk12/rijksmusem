package com.example.maxdororecruitmenttask.domain.entity

data class Art(
    val artIdentifier: ArtIdentifier,
    val artDescriptionInfo: ArtDescriptionInfo,
)

data class ArtIdentifier(
    val objectNumber: String,
)

data class ArtDescriptionInfo(
    val title: String,
    val principalOrFirstMaker: String,
    val webImageUrl: String,
)
