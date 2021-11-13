package com.amfysy.bicloodroid.data.api.dtos

import com.google.gson.annotations.SerializedName

   
data class FacetGroupsDto (

   @SerializedName("facets") var facets : List<FacetsDto>,
   @SerializedName("name") var name : String

)