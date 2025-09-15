package com.medics.zmed.domain.repository

import com.medics.zmed.persistance.entity.CategoryDao
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface CategoryRepository {

    fun getAllCategory() : List<CategoryDao>?


    fun saveCategory(categoryDao: List <CategoryDao>) : Boolean

}


@Repository
class CategoryRepositoryImpl(private val repository: SpringCategoryRepository) : CategoryRepository{
    override fun getAllCategory(): List<CategoryDao>? {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"))

    }

    override fun saveCategory(categoryDao: List<CategoryDao>): Boolean {

        val category = repository.saveAll(categoryDao)

        return category.isNotEmpty()
    }

}


interface  SpringCategoryRepository : JpaRepository<CategoryDao, Long> {

}