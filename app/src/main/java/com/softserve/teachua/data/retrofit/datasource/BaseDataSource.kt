package com.softserve.teachua.data.retrofit.datasource

import com.softserve.teachua.app.tools.Resource
import retrofit2.Response

//
//
//IS NOT USED RIGHT NOW
//
//
@Suppress("UNREACHABLE_CODE")
abstract class BaseDataSource{
    protected suspend fun <T> getResult(call: suspend () -> Response<T>) : Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful){
                val body = response.body()
                if(body!=null) return Resource.success(body)
            }

            return error("${response.code()} ${response.message()}")
        } catch (e: Exception){
            return Resource.error(data = null, message = e.message)
        }
    }
}