package com.example.tourguideplus.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.commit
import com.example.tourguideplus.MainActivity
import com.example.tourguideplus.HelpActivity
import com.example.tourguideplus.R
import com.example.tourguideplus.navigation.Screen
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HostActivity : AppCompatActivity() {                       // ← наследуем от AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)
        val composeContainer  = findViewById<ComposeView>(R.id.compose_container)
        val bottomNav         = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val fab               = findViewById<FloatingActionButton>(R.id.fab_add)

        // Обработчик кнопок табов
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_places -> {
                    showClassicPlaces()
                    true
                }
                R.id.nav_routes -> {
                    showComposeScreen(Screen.Routes.route)
                    true
                }
                R.id.nav_favorites -> {
                    showComposeScreen(Screen.Favorites.route)
                    true
                }
                R.id.nav_weather -> {
                    showComposeScreen(Screen.Weather.route)
                    true
                }
                R.id.nav_help -> {
                    startActivity(Intent(this, HelpActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Обработчик FAB
        fab.setOnClickListener {
            when (bottomNav.selectedItemId) {
                R.id.nav_places   -> showComposeScreen(Screen.PlaceForm.createRoute(null))
                R.id.nav_routes   -> showComposeScreen(Screen.RouteForm.createRoute(null))
            }
        }

        // Запускаем Places классическим списком по умолчанию
        bottomNav.selectedItemId = R.id.nav_places
    }

    private fun showClassicPlaces() {
        // 1) Показываем фрагмент, скрываем Compose
        findViewById<FrameLayout>(R.id.fragment_container).visibility = VISIBLE
        findViewById<ComposeView>(R.id.compose_container).visibility  = GONE

        // 2) Подставляем PlacesListFragment
        supportFragmentManager.commit {
            replace(R.id.fragment_container, PlacesListFragment())
        }
    }

     fun showComposeScreen(route: String) {
        // 1) Показываем Compose, скрываем класс. фрагмент
        findViewById<FrameLayout>(R.id.fragment_container).visibility = GONE
        findViewById<ComposeView>(R.id.compose_container).visibility  = VISIBLE

        // 2) Встраиваем Compose-контент в ComposeView
        findViewById<ComposeView>(R.id.compose_container).setContent {
            HostComposeContent(startRoute = route)
        }
    }
}
