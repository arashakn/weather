package com.twitter.twitterchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twitter.twitterchallenge.model.Weather
import com.twitter.twitterchallenge.model.WeatherCondition
import com.twitter.twitterchallenge.newtwork.WeatherAPIClient
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.RuntimeException
import java.util.ArrayList

class WeatherViewModel  : ViewModel(){
    val weatherLiveData = MutableLiveData<WeatherCondition>()
    val sdLiveData = MutableLiveData<Double>()
    val error = MutableLiveData<Boolean>()
    val progress = MutableLiveData<Boolean>()
    val compositeDisposable  = CompositeDisposable ()

    init {
        fetchCurrentWeather()
    }

    fun fetchCurrentWeather(){
        error.value = false
        progress.value = true
        try {
            val disposable = WeatherAPIClient.getWeatherService().getCurrentWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        weatherLiveData.value = result
                        progress.value = false
                    },
                    { e ->
                        error.value = true
                        progress.value = false
                    }
                )
            compositeDisposable.add(disposable)
        }catch (e : RuntimeException){
            error.value = true
            progress.value = false
        }
    }

    fun getNextNDaysObservable(){

            val disposable1 = WeatherAPIClient.getWeatherService().getFutureWeather(1.getFutureWeatherUrl())
            val disposable2 = WeatherAPIClient.getWeatherService().getFutureWeather(2.getFutureWeatherUrl())
            val disposable3 = WeatherAPIClient.getWeatherService().getFutureWeather(3.getFutureWeatherUrl())
            val disposable4 = WeatherAPIClient.getWeatherService().getFutureWeather(4.getFutureWeatherUrl())
            val disposable5 = WeatherAPIClient.getWeatherService().getFutureWeather(5.getFutureWeatherUrl())

            val zipped = Singles.zip(disposable1,disposable2 ,disposable3, disposable4,disposable5){
                    disposable1,disposable2 ,disposable3, disposable4,disposable5 ->
                    val temps = ArrayList<Float>()
                    temps.add(disposable1.weather.temp)
                    temps.add(disposable2.weather.temp)
                    temps.add(disposable3.weather.temp)
                    temps.add(disposable4.weather.temp)
                    temps.add(disposable5.weather.temp)
                    calculateSD(temps.toFloatArray())
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        progress.value = false
                        sdLiveData.value = result


                    },
                    { e ->
                        error.value = true
                        progress.value = false
                    }
                )
        compositeDisposable.add(zipped)
        }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.let {
            compositeDisposable.clear()
        }
    }
}