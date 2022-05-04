package com.softserve.teachua.data.retrofit

import com.softserve.teachua.app.DomainModule
import com.softserve.teachua.app.baseUrl
import retrofit2.Retrofit

object Common {

    val retrofitService: RetrofitService
        get() = RetrofitClient().getClient()



}
