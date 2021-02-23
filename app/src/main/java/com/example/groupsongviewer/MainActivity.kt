package com.example.groupsongviewer

import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.nio.file.Files
import java.nio.file.Files.isDirectory
import java.nio.file.Path

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get everything in the assets folder
        val textList = DataSource(this).getTextAssets()

        //Do the recycler view thing
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = SongFileAdapter(textList)

    }
}