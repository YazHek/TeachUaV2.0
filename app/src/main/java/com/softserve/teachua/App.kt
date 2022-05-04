package com.softserve.teachua

import android.app.Application
import com.softserve.teachua.retrofit.Common
import com.softserve.teachua.service.BannersService

class App : Application() {

    var bannersService = BannersService()
}