package com.softserve.teachua.service

import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.app.tools.mapper.toCategory
import com.softserve.teachua.data.model.CategoryModel
import com.softserve.teachua.data.retrofit.RetrofitService
import javax.inject.Inject

class CategoriesService @Inject constructor(
    private val retrofitService: RetrofitService,
) {


    suspend fun getAllCategories(): Resource<List<CategoryModel>> {

        val categoriesResponse = retrofitService.getAllCategories()
        return if (categoriesResponse.isSuccessful) {

            val categories = checkNotNull(categoriesResponse.body()).map { it.toCategory() }
            Resource.success(categories)
        } else
            Resource.error()

    }
}