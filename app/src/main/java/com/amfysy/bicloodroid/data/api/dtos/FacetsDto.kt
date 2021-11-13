package com.amfysy.bicloodroid.data.api.dtos

import com.google.gson.annotations.SerializedName

   
data class FacetsDto (

   @SerializedName("count") var count : Int,
   @SerializedName("path") var path : String,
   @SerializedName("state") var state : String,
   @SerializedName("name") var name : String

)