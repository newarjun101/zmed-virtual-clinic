package com.medics.zmed.application.service

import com.medics.zmed.application.mapper.toCategoryDaoModelList

import com.medics.zmed.application.mapper.toCategoryResponseModelList
import com.medics.zmed.domain.model.request_model.CategoryRequestModel
import com.medics.zmed.domain.model.response_model.CategoryResponseModel
import com.medics.zmed.domain.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService (private val categoryRepository: CategoryRepository) {


    fun getResponseModel() : List<CategoryResponseModel>? {
       val categoryDaoList =  categoryRepository.getAllCategory()
        if(categoryDaoList.isNullOrEmpty())  {
            return emptyList()
        }
        return  categoryDaoList.toCategoryResponseModelList()
    }

    fun onSaveCategory(categoryRequestModel: List<CategoryRequestModel>)  {

        val categoryDao = categoryRequestModel.toCategoryDaoModelList()

        val addedCategory = categoryRepository.saveCategory(categoryDao)

        if(!addedCategory) {
            throw IllegalArgumentException("Wrong data or invalid input")
        }
    }
}