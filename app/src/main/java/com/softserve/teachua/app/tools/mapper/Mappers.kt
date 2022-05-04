package com.softserve.teachua.app.tools.mapper

import com.softserve.teachua.data.dto.*
import com.softserve.teachua.data.model.*

internal fun BannersDto.toBanner(): BannerModel {
    return BannerModel(
        bannerId = id,
        bannerTitle = title,
        bannerSubtitle = subtitle,
        bannerLink = link,
        bannerPicture = picture,
        bannerSequenceNumber = sequenceNumber
    )

}

internal fun CategoriesDro.toCategory(): CategoryModel {
    return CategoryModel(
        categoryId = id,
        categorySortby = sortby,
        categoryName = name,
        categoryDescription = description,
        categoryUrlLogo = urlLogo,
        categoryBackgroundColor = backgroundColor,
        categoryTagBackgroundColor = tagBackgroundColor,
        categoryTagTextColor = tagTextColor
    )

}

internal fun ClubDescriptionDto.toClub(): ClubModel {
    return ClubModel(
        clubId = id,
        clubName = name,
        clubDescription = description,
        clubImage = categories[0].urlLogo,
        clubBackgroundColor = categories[0].backgroundColor,
        clubCategoryName = categories[0].name
    )

}

internal fun CitiesDto.toCity(): CityModel {
    return CityModel(
        cityId = id,
        cityName = name,
        cityLatitude = latitude,
        cityLongtitude = longtitude
    )
}

internal fun DistrictsDto.toDistrict(): DistrictModel {
    return DistrictModel(
        districtId = id,
        districtName = name,
        cityName = cityName
    )
}

internal fun StationsDto.toStation(): StationModel {
    return StationModel(
        stationId = id,
        stationName = name,
        cityName = cityName,
        districtName = districtName
    )
}
