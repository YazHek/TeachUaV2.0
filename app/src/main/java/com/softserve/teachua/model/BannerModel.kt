package com.softserve.teachua.model

data class BannerModel(
    var bannerId: Int,
    var bannerTitle: String,
    var bannerSubtitle: String?,
    var bannerLink: String?,
    var bannerPicture: String?,
    var bannerSequenceNumber: Int?
)
