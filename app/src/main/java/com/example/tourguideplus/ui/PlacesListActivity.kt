package com.example.tourguideplus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tourguideplus.R

class PlacesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_list)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlacesListFragment())
                .commit()
        }
    }
}
