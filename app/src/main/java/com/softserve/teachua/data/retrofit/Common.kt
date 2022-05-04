package com.softserve.teachua.data.retrofit

import com.softserve.teachua.app.baseUrl

object Common {

    val retrofitService: RetrofitService
        get() =
            RetrofitClient.getClient(baseUrl).create(RetrofitService::class.java)


}
