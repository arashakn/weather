package com.twitter.challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : AppCompatActivity() {
    lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setupViews()
        observe()
    }

    private fun setupViews(){
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        btn_median_next_five_days.setOnClickListener{
            weatherViewModel.getNextNDaysObservable()
        }
    }


    private  fun observe(){
        weatherViewModel.weatherLiveData.observe(this, Observer {

                it?.let {
                    cv_current.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE

                    it.name?.let {
                        tv_location.text = it.toString()
                    }

                    it.weather?.temp?.let {
                        tv_weather_temp.visibility = View.VISIBLE
                        tv_weather_temp.text = "Temperature : ${it.toString()} \u2109" + " \\ " +"${TemperatureConverter.celsiusToFahrenheit(it).toInt()} \u2103"
                                }?:kotlin.run {
                                    tv_weather_temp.visibility = View.GONE
                                }
                    it.wind?.speed?.let {
                        tv_wind_speed.visibility = View.VISIBLE
                        tv_wind_speed.text ="Wind Speed : ${it.toString()}"
                    }?:kotlin.run {
                        tv_wind_speed.visibility = View.GONE
                    }
                    it.clouds?.cloudiness?.let{
                        iv_sunny_cloudy.visibility = View.VISIBLE
                        when(it <=50){
                            true -> iv_sunny_cloudy.setImageResource(R.drawable.ic_wb_sunny)
                            false -> iv_sunny_cloudy.setImageResource(R.drawable.ic_cloud)
                        }
                    }?:kotlin.run {
                        iv_sunny_cloudy.visibility = View.GONE
                    }
                } ?:kotlin.run{
                    cv_current.visibility = View.GONE
                }
        })

        weatherViewModel.error.observe(this, Observer {
            when(it){
                true ->
                {
                    cv_current.visibility = View.GONE
                    tv_error.visibility = View.VISIBLE
                }

                false ->
                {
                    cv_current.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                }
            }

        })
        weatherViewModel.progress.observe(this, Observer {
            when(it){
                true  -> {
                    progress_circular.visibility =View.VISIBLE
                    cv_current.visibility = View.GONE
                }
                false -> progress_circular.visibility =View.GONE
            }
        })
        weatherViewModel.sdLiveData.observe(this, Observer {
            sd_five_days.visibility = View.VISIBLE
            sd_five_days.text = "Standard division of weather for next 5 days is : ${"%.2f".format(it)}"
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.refresh -> {
                weatherViewModel.fetchCurrentWeather()
                true
            }
            else -> false
        }
    }
}
