package com.example.groupsongviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //val settingsFragment = findViewById<FragmentContainerView>(R.id.settings_fragment)
        //supportFragmentManager.beginTransaction().add(R.id.settings_fragment,SettingsFragment()).commit()
    }
}