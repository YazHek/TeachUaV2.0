package com.softserve.teachua.app.tools

class CategoryToUrlTransformer {

    fun toUrlEncode(categoryName: String): String {

        return if (categoryName.contains(", ")) {
            categoryName.replace(", ", ";+")
        } else
            return categoryName


    }
}