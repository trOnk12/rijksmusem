package com.example.maxdoroassigment.presentation.mapper

import com.example.maxdoroassigment.core.Mapper
import com.example.maxdoroassigment.domain.entity.Art
import com.example.maxdoroassigment.presentation.model.ArtListItem
import javax.inject.Inject

class ArtMapper @Inject constructor() : Mapper<Art, ArtListItem> {
    override fun map(input: Art): ArtListItem {
        with(input) {
            return ArtListItem(
                id,
                artDescriptionInfo.title,
                artDescriptionInfo.webImageUrl
            )
        }
    }
}