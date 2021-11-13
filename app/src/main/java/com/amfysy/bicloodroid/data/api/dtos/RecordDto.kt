package com.amfysy.bicloodroid.data.api.dtos

import com.google.gson.annotations.SerializedName

data class RecordDto(

    @SerializedName("datasetid") var datasetid: String,
    @SerializedName("recordid") var recordid: String,
    @SerializedName("fields") var fields: FieldsDto,
    @SerializedName("record_timestamp") var recordTimestamp: String

)