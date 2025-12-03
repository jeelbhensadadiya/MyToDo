package com.jeelpatel.mytodo.domain.model

data class WeatherResponseModel(
    val location: LocationModel,
    val current: CurrentModel
)


data class LocationModel(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localtime_epoch: Long,
    val localtime: String
)


data class CurrentModel(
    val last_updated_epoch: Long,
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double,
    val is_day: Int,
    val condition: ConditionModel,
    val wind_mph: Double,
    val wind_kph: Double,
    val wind_degree: Int,
    val wind_dir: String,
    val pressure_mb: Double,
    val pressure_in: Double,
    val precip_mm: Double,
    val precip_in: Double,
    val humidity: Int,
    val cloud: Int,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val windchill_c: Double,
    val windchill_f: Double,
    val heatindex_c: Double,
    val heatindex_f: Double,
    val dewpoint_c: Double,
    val dewpoint_f: Double,
    val vis_km: Double,
    val vis_miles: Double,
    val uv: Double,
    val gust_mph: Double,
    val gust_kph: Double,
    val air_quality: AirQualityModel,
    val short_rad: Double,
    val diff_rad: Double,
    val dni: Double,
    val gti: Double
)


data class ConditionModel(
    val text: String,
    val icon: String,
    val code: Int
)


data class AirQualityModel(
    val co: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    val pm2_5: Double,
    val pm10: Double,
    val us_epa_index: Double,
    val gb_defra_index: Double,
)