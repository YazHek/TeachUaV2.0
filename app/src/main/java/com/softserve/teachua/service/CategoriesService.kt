package com.softserve.teachua.service

import com.softserve.teachua.mapper.toCategory
import com.softserve.teachua.model.CategoryModel
import com.softserve.teachua.retrofit.Common

class CategoriesService() {

    var retrofitService = Common.retrofitService

    suspend fun getAllCategories(): List<CategoryModel> {

        lateinit var categories: List<CategoryModel>
        val categoriesResponse = retrofitService.getAllCategories()
        if (categoriesResponse.isSuccessful) {

            categories = checkNotNull(categoriesResponse.body()).map { it.toCategory() }
        }
        return categories

    }
}