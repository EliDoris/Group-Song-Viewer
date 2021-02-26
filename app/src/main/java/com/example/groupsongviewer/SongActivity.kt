package com.example.groupsongviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SongActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        //Get the intent that started the activity, and get its song info object
        val sInfo = intent.getSerializableExtra(EXTRA_SONGINFO) as SongInfo

        //Set the textView
        findViewById<TextView>(R.id.verse_text).apply{
            text = sInfo.verses[0]
        }
    }
}