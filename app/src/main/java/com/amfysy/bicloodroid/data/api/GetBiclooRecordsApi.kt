package com.amfysy.bicloodroid.data.api

import com.amfysy.bicloodroid.data.api.dtos.BiclooRecordsResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetBiclooRecordsApi {
    @GET("/api/records/1.0/search/?dataset=244400404_stations-velos-libre-service-nantes-metropole&q=&facet=commune&amp;facet=descriptif")
    fun getRecords(): Call<BiclooRecordsResponseDto>
}