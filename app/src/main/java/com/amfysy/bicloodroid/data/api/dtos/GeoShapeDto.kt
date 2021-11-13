package com.amfysy.bicloodroid.data.api.dtos

import com.google.gson.annotations.SerializedName

   
data class GeoShapeDto (

   @SerializedName("type") var type : String,
   @SerializedName("coordinates") var coordinates : List<Double>

)