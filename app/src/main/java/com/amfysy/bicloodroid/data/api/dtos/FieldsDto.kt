package com.amfysy.bicloodroid.data.api.dtos

import com.google.gson.annotations.SerializedName

   
data class FieldsDto (

   @SerializedName("nom") var nom : String,
   @SerializedName("exploitant") var exploitant : String,
   @SerializedName("insee") var insee : String,
   @SerializedName("categorie") var categorie : String,
   @SerializedName("commune") var commune : String,
   @SerializedName("adresse") var adresse : String,
   @SerializedName("lien") var lien : String,
   @SerializedName("localisation") var localisation : String,
   @SerializedName("geo_shape") var geoShape : GeoShapeDto,
   @SerializedName("gid") var gid : String,
   @SerializedName("ouverture") var ouverture : String,
   @SerializedName("capacite_num") var capaciteNum : Int,
   @SerializedName("capacite") var capacite : String,
   @SerializedName("tel") var tel : String,
   @SerializedName("cp") var cp : String,
   @SerializedName("conditions") var conditions : String,
   @SerializedName("ss_categorie") var ssCategorie : String,
   @SerializedName("source") var source : String

)