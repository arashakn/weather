package com.twitter.twitterchallenge

fun Int.getFutureWeatherUrl() = "future_${this.toString()}.json"

fun calculateSD(numArray: FloatArray): Double {
    var sum = 0.0
    var standardDeviation = 0.0
    val length = numArray.size
    for (num in numArray) {
        println("$num << ")
        sum += num.toDouble()
    }
    val mean = sum / length
    for (num in numArray) {
        standardDeviation += Math.pow(num - mean, 2.0)
    }
    return Math.sqrt(standardDeviation / length)
}