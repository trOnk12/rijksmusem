package com.example.maxdoroassigment.data.source.remote

import com.example.maxdoroassigment.data.api.RijksMuseumApi
import com.example.maxdoroassigment.data.mapper.ArtResponseMapper
import com.example.maxdoroassigment.domain.entity.Art
import javax.inject.Inject

interface ArtRemoteSource {
    suspend fun getArtByPage(page: Int): List<Art>

}

class ArtRemoteSourceRijksMuseumService
@Inject constructor(
    private val rijksMuseumApi: RijksMuseumApi,
    private val mapperRemote: ArtResponseMapper
) : ArtRemoteSource {

    override suspend fun getArtByPage(page: Int): List<Art> =
        rijksMuseumApi.getArtByPage(page).run { mapperRemote.map(this) }

}