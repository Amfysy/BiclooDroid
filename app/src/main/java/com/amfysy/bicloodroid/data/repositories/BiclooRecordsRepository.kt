package com.amfysy.bicloodroid.data.repositories

import com.amfysy.bicloodroid.data.api.GetBiclooRecordsApi
import com.amfysy.bicloodroid.data.model.BiclooRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BiclooRecordsRepository {
    private val apiInstance = Retrofit.Builder()
        .baseUrl("https://data.nantesmetropole.fr")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GetBiclooRecordsApi::class.java)

    suspend fun getRecords(): Result<List<BiclooRecord>> = withContext(Dispatchers.IO) {
        try {
            val call = apiInstance.getRecords()
            val recordsResponse =
                call.execute().body() ?: return@withContext Result.failure(Exception("Empty Body"))
            recordsResponse.records.map { recDto ->
                BiclooRecord(
                    recDto.fields.nom,
                    recDto.fields.geoShape.coordinates[0],
                    recDto.fields.geoShape.coordinates[1],
                    recDto.fields.capaciteNum
                )
            }.let { Result.success(it) }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}