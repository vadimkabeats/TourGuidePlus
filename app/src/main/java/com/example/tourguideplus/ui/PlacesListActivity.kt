package com.example.tourguideplus.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tourguideplus.HelpActivity
import com.example.tourguideplus.MainActivity
import com.example.tourguideplus.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlacesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_list)

        // Фрагмент со списком
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PlacesListFragment())
                .commit()
        }

        // FAB «Добавить место» → MainActivity (Compose) в режим добавления
        findViewById<FloatingActionButton>(R.id.fab_add_place)
            .setOnClickListener {
                startActivity(
                    Intent(this, MainActivity::class.java)
                        .putExtra("open_tab", "add_place")
                )
            }

        // BottomNavigationView
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_places -> {
                        // мы уже здесь
                        true
                    }
                    R.id.nav_routes -> {
                        startMainTab("routes"); true
                    }
                    R.id.nav_favorites -> {
                        startMainTab("favorites"); true
                    }
                    R.id.nav_weather -> {
                        startMainTab("weather"); true
                    }
                    R.id.nav_help -> {
                        startActivity(Intent(this, HelpActivity::class.java)); true
                    }
                    else -> false
                }
            }
    }

    private fun startMainTab(tab: String) {
        startActivity(
            Intent(this, MainActivity::class.java)
                .putExtra("open_tab", tab)
        )
    }
}


