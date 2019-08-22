package com.twitter.twitterchallenge

fun Int.getFutureWeatherUrl() = "future_${this.toString()}.json"
