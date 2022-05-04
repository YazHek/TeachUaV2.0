package com.softserve.teachua.app

import android.app.Application
import com.softserve.teachua.service.BannersService
import dagger.hilt.android.HiltAndroidApp

const val baseImageUrl = "https://speak-ukrainian.org.ua/dev/"
const val baseUrl = "https://speak-ukrainian.org.ua/dev/api/"

@HiltAndroidApp
class App : Application()