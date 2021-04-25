package com.example.maxdoroassigment.data.mapper

import com.example.maxdoroassigment.core.Mapper
import com.example.maxdoroassigment.data.api.model.ArtResponse
import com.example.maxdoroassigment.domain.entity.Art
import com.example.maxdoroassigment.domain.entity.ArtDescriptionInfo
import javax.inject.Inject

class ArtResponseMapper @Inject constructor() : Mapper<ArtResponse, List<Art>> {
    override fun map(input: ArtResponse): List<Art> {
        return input.artObjectObjects.map { artRemoteObject ->
            with(artRemoteObject) {
                Art(
                    id = id,
                    artDescriptionInfo = ArtDescriptionInfo(
                        title = title,
                        principalOrFirstMaker = principalOrFirstMaker,
                        webImageUrl = headerImage.url
                    )
                )
            }
        }
    }
}