package com.brain.systemweather

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
//{"coord":{"lon":74.3436,"lat":31.5497},"weather":[{"id":711,"main":"Smoke","description":"smoke","icon":"50n"}],"base":"stations","main":{"temp":295.14,"feels_like":295.3,"temp_min":291.21,"temp_max":295.14,"pressure":1012,"humidity":73,"sea_level":1012,"grnd_level":988},"visibility":1500,"wind":{"speed":0,"deg":0},"clouds":{"all":5},"dt":1731509335,"sys":{"type":1,"id":7585,"country":"PK","sunrise":1731461336,"sunset":1731499506},"timezone":18000,"id":1172451,"name":"Lahore","cod":200}
//1868d35fcb994be56cd847f9c0292e78
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}