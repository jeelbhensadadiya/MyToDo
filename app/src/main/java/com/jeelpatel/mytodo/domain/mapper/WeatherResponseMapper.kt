package com.jeelpatel.mytodo.domain.mapper

import com.jeelpatel.mytodo.data.remote.dto.ConditionDto
import com.jeelpatel.mytodo.data.remote.dto.CurrentDto
import com.jeelpatel.mytodo.data.remote.dto.LocationDto
import com.jeelpatel.mytodo.data.remote.dto.WeatherResponseDto
import com.jeelpatel.mytodo.domain.model.ConditionModel
import com.jeelpatel.mytodo.domain.model.CurrentModel
import com.jeelpatel.mytodo.domain.model.LocationModel
import com.jeelpatel.mytodo.domain.model.WeatherResponseModel

fun WeatherResponseDto.toDomain(): WeatherResponseModel {
    return WeatherResponseModel(
        location = location.toDomain(),
        current = current.toDomain()
    )
}


fun LocationDto.toDomain(): LocationModel {
    return LocationModel(
        name = name,
        region = region,
        country = country,
        lat = lat,
        lon = lon,
        tz_id = tz_id,
        localtime_epoch = localtime_epoch,
        localtime = localtime
    )
}


fun CurrentDto.toDomain(): CurrentModel {
    return CurrentModel(
        last_updated_epoch = last_updated_epoch,
        last_updated = last_updated,
        temp_c = temp_c,
        temp_f = temp_f,
        is_day = is_day,
        condition = condition.toDomain(),
        wind_mph = wind_mph,
        wind_kph = wind_kph,
        wind_degree = wind_degree,
        wind_dir = wind_dir,
        pressure_mb = pressure_mb,
        pressure_in = pressure_in,
        precip_mm = precip_mm,
        precip_in = precip_in,
        humidity = humidity,
        cloud = cloud,
        feelslike_c = feelslike_c,
        feelslike_f = feelslike_f,
        windchill_c = windchill_c,
        windchill_f = windchill_f,
        heatindex_c = heatindex_c,
        heatindex_f = heatindex_f,
        dewpoint_c = dewpoint_c,
        dewpoint_f = dewpoint_f,
        vis_km = vis_km,
        vis_miles = vis_miles,
        uv = uv,
        gust_mph = gust_mph,
        gust_kph = gust_kph,
        short_rad = short_rad,
        diff_rad = diff_rad,
        dni = dni,
        gti = gti
    )
}

fun ConditionDto.toDomain(): ConditionModel {
    return ConditionModel(
        text = text,
        icon = icon,
        code = code
    )
}
