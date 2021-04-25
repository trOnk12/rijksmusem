package com.example.maxdoroassigment.data.api

import com.example.maxdoroassigment.data.api.model.ArtResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RijksMuseumApi {

    @GET("collection")
    suspend fun getArtByPage(@Query("p") page: Int): ArtResponse

}