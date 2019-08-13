package com.twitter.twitterchallenge.model

import com.google.gson.annotations.SerializedName

data class WeatherCondition (
    @SerializedName ("coord") val coordination : Coordination,
    val weather : Weather , val wind : Wind,
    val rain : Rain, val clouds: Clouds ,val name : String
)

data class Coordination (val lon : Float, val lat :Float)

data class Weather (val temp : Float, val pressure : Int , val humidity : Int)

data class Wind(val speed : Float, val deg : Int)

data class Rain (@SerializedName("3h") val threeHours : Int )

data class Clouds ( val cloudiness : Int )

