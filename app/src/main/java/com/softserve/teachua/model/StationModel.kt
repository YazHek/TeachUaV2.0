package com.softserve.teachua.model

data class StationModel(
    var stationId: Int,
    var stationName: String,
    var cityName: String,
    var districtName: String?,
) {
}