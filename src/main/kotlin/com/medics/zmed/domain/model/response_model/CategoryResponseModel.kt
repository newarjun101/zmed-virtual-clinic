package com.medics.zmed.domain.model.response_model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.context.annotation.Description

data class CategoryResponseModel(
    val id : Long,
    val name : String,
    val description: String? = null,
    @JsonProperty("image_url")
    val imageUrl : String? = null
)