package com.medics.zmed.domain.model.request_model

import com.fasterxml.jackson.annotation.JsonProperty


data class CategoryRequestModel(
    val id : Long,
    val name : String,
    val description: String? = null,
    @JsonProperty("image_url")
    val imageUrl : String? = null
)