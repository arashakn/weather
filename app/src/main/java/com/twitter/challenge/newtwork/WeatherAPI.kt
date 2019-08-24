package com.twitter.challenge.newtwork

import com.twitter.challenge.model.WeatherCondition
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface WeatherAPI {
    @GET("current.json")
    fun getCurrentWeather() : Single<WeatherCondition>

    @GET
    fun getFutureWeather(@Url url : String) : Single<WeatherCondition>
}