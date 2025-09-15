package com.medics.zmed.controller

import com.medics.zmed.application.mapper.response_model_mapper.toSuccessModel
import com.medics.zmed.application.service.CategoryService
import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.domain.model.request_model.CategoryRequestModel
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/category")
@Tag(name = "Category Related API", description = "Category to manage Auth")
class CategoryController (private val categoryService: CategoryService) {

    @GetMapping("/get-all-category")
    fun getAllCategory() : ResponseEntity<ResponseModel> {

        val categoryList = categoryService.getResponseModel()?:emptyList()

        return ResponseEntity.status(HttpStatus.OK).body(categoryList.toSuccessModel())

    }

    @PostMapping("/add-category-list")
    fun saveCategoryList(@RequestBody category : List<CategoryRequestModel>? = null) : ResponseEntity<ResponseModel> {


        if(category.isNullOrEmpty()) {
            throw IllegalArgumentException("Please send valid json")
        }
        categoryService.onSaveCategory(category)
        //  val categoryList = categoryService.getResponseModel()?:emptyList()

        return ResponseEntity.status(HttpStatus.OK).body("Success".toSuccessModel(message = "Successful",))

    }
}