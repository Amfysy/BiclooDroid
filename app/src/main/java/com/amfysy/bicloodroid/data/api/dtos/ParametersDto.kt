package com.amfysy.bicloodroid.data.api.dtos

import com.google.gson.annotations.SerializedName

   
data class ParametersDto (

   @SerializedName("dataset") var dataset : String,
   @SerializedName("timezone") var timezone : String,
   @SerializedName("rows") var rows : Int,
   @SerializedName("start") var start : Int,
   @SerializedName("format") var format : String,
   @SerializedName("facet") var facet : List<String>

)