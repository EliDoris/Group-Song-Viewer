package com.example.groupsongviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongFileAdapter(private val songList: Array<SongInfo>):
    RecyclerView.Adapter<SongFileAdapter.SongViewHolder>() {

    // Describes an item view and its place within the RecyclerViewer
    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val songTitleView: TextView = itemView.findViewById(R.id.title_text)
        private val songArtistView: TextView = itemView.findViewById(R.id.artist_text)

        fun bind(sInfo: SongInfo) {
            songTitleView.text = sInfo.title
            songArtistView.text = sInfo.artist
        }
    }

    //Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.title_artist_item, parent, false)

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