package com.twitter.twitterchallenge.newtwork

import com.twitter.twitterchallenge.model.Weather
import com.twitter.twitterchallenge.model.WeatherCondition
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface WeatherAPI {
    @GET("current.json")
    fun getCurrentWeather() : Single<WeatherCondition>
}