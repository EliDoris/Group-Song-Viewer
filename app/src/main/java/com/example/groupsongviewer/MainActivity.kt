package com.example.groupsongviewer

import android.content.Intent
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.nio.file.Files
import java.nio.file.Files.isDirectory
import java.nio.file.Path

//Identifier constant
const val EXTRA_SONGINFO = "com.example.groupsongviewer.SONGINFO"

class MainActivity : AppCompatActivity(), CellClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Alternate method for getting song info
        val infoList = DataSource(this).getSongInfo()
        //val textList = getTitles(infoList)

        //Do the recycler view thing
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = SongFileAdapter(infoList,this)

    }

    //This happens any time a song title/artist is clicked
    override fun onCellClickListener(sInfo: SongInfo) {

        //Create a new intent and activity
        val intent = Intent(this,SongActivity::class.java).apply{
            putExtra(EXTRA_SONGINFO,sInfo)
        }
        startActivity(intent)
    }
}