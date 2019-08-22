package com.twitter.twitterchallenge.newtwork

import com.twitter.twitterchallenge.model.Weather
import com.twitter.twitterchallenge.model.WeatherCondition
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface WeatherAPI {
    @GET("current.json")
    fun getCurrentWeather() : Single<WeatherCondition>

    @GET
    fun getFutureWeather(@Url url : String) : Single<WeatherCondition>
}