package com.example.tourguideplus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tourguideplus.ui.HelpFragment

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HelpFragment())
                .commit()
        }
    }
}