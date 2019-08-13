package com.twitter.twitterchallenge.newtwork

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.twitter.twitterchallenge.WeatherApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object WeatherAPIClient {

    const val BASE_URL = "https://twitter-code-challenge.s3.amazonaws.com"
    const val cacheSize = (5 * 1024 * 1024).toLong()
    var myCache = Cache(WeatherApplication.applicationContext().cacheDir, cacheSize)

    private val weatherAPI : WeatherAPI

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(60,  TimeUnit.SECONDS)
            .readTimeout(60,  TimeUnit.SECONDS)
            .cache(myCache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(WeatherApplication.applicationContext())!!)
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        weatherAPI = retrofit.create(WeatherAPI::class.java)
    }

    fun getWeatherService() : WeatherAPI = weatherAPI

    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

}