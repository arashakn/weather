package com.twitter.twitterchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twitter.twitterchallenge.model.Weather
import com.twitter.twitterchallenge.model.WeatherCondition
import com.twitter.twitterchallenge.newtwork.WeatherAPIClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.lang.RuntimeException
import java.util.ArrayList

class WeatherViewModel  : ViewModel(){
    val weatherLiveData = MutableLiveData<WeatherCondition>()
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
                .subscribeWith(object  : DisposableSingleObserver<WeatherCondition>(){
                    override fun onSuccess(value: WeatherCondition?) {
                        weatherLiveData.value = value
                        progress.value = false
                    }

                    override fun onError(e: Throwable?) {
                        error.value = true
                        progress.value = false
                    }
                })
            compositeDisposable.add(disposable)
        }catch (e : RuntimeException){
            error.value = true
            progress.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.let {
            compositeDisposable.clear()
        }
    }

}