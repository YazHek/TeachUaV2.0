package com.softserve.teachua.retrofit

object Common {
    private val BASE_URL = "https://speak-ukrainian.org.ua/dev/"

    val retrofitService: RetrofitService
        get() =
            RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)


}
