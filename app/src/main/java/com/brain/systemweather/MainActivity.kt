package com.brain.systemweather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.brain.systemweather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWeatherData("Lahore")  // Initial data load for Lahore
        searchCity()  // Sets up search functionality
    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    fetchWeatherData(query)  // Pass the query string here
                    searchView.clearFocus()  // Hide the keyboard after submitting
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // You can handle text change events here if needed
                return true
            }
        })
    }

    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val call = retrofit.getDataWeather(cityName, "1868d35fcb994be56cd847f9c0292e78", "metric")

        call.enqueue(object : Callback<WeatherApp> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    val temperature = responseBody?.main?.temp.toString()
                    val humidity = responseBody?.main?.humidity
                    val windSpeed = responseBody?.wind?.speed
                    val sunrise = responseBody?.sys?.sunrise
                    val sunset = responseBody?.sys?.sunset
                    val seaLevel = responseBody?.main?.pressure
                    val condition = responseBody?.weather?.firstOrNull()?.main ?: "Unknown"
                    val maxTemp = responseBody?.main?.temp_max
                    val minTemp = responseBody?.main?.temp_min

                    // Update UI with weather information
                    binding.temp.text = "$temperature °C"
                    binding.weather.text = condition
                    binding.maxtemp.text = "Max Temp: $maxTemp °C"
                    binding.mintemp.text = "Min Temp: $minTemp °C"
                    binding.humanity.text = "$humidity %"
                    binding.windspeed.text = "$windSpeed m/s"
                    binding.sunrise.text = "$sunrise"
                    binding.sunset.text = "$sunset"
                    binding.sea.text = "$seaLevel HPA"
                    binding.condition.text = condition
                    binding.textView7.text = dayName()
                    binding.cityname.text = cityName
                    binding.day.text = date()

                  //  Log.d("TAG", "Weather data fetched successfully for $cityName: $temperature °C")
                    changeImageAccordingToCondition(condition)
                } else {
                    Log.e("TAG", "Response unsuccessful or empty body: ${response.errorBody()}")
                    Toast.makeText(this@MainActivity, "Data not retrieved. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Log.e("TAG", "API call failed: ${t.message}")
                Toast.makeText(this@MainActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun changeImageAccordingToCondition(conditions: String) {
        when(conditions)
        {
            "Partly Clouds","Clouds","Overcast","Mist","Foggy"->
            {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Clear Sky","Sunny","Clear"->
            {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Light rain","Drizzle","Moderate rain ","Shower","Heavy rain"->
            {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow","Moderate Snow","Heavy Snow","Blizzard"->
            {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
        }
        binding.lottieAnimationView.playAnimation()

    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun dayName(): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
}
