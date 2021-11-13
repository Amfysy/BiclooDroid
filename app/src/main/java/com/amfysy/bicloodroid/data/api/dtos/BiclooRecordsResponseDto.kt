package com.amfysy.bicloodroid.data.api.dtos

import com.google.gson.annotations.SerializedName

   
data class BiclooRecordsResponseDto (
   @SerializedName("nhits") var nhits : Int,
   @SerializedName("parameters") var parameters : ParametersDto,
   @SerializedName("records") var records : List<RecordDto>,
   @SerializedName("facet_groups") var facetGroups : List<FacetGroupsDto>

)