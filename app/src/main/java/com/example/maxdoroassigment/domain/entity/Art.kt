package com.example.maxdoroassigment.domain.entity

data class Art(
    val id: String,
    val artDescriptionInfo: ArtDescriptionInfo
)

data class ArtDescriptionInfo(
    val title: String,
    val principalOrFirstMaker: String,
    val webImageUrl: String,
)
