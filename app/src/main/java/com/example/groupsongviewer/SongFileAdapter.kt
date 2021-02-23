package com.example.groupsongviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongFileAdapter(val songList: Array<String>): RecyclerView.Adapter<SongFileAdapter.SongViewHolder>() {

    // Describes an item view and its place within the RecyclerViewer
    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTextView: TextView = itemView.findViewById(R.id.song_text)

        fun bind(word: String) {
            songTextView.text = word
        }
    }

    //Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_item, parent, false)

        return SongViewHolder(view)
    }

    //Returns the size of the data list
    override fun getItemCount(): Int {
        return songList.size
    }

    //Display data at a certain position
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songList[position])
    }
}