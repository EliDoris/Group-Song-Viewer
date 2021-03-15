package com.example.groupsongviewer

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView

class SongFileAdapter(private var songList: Array<SongInfo>, private val cellClickListener: CellClickListener, adapterContext: Context):
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
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(songList[position])
        }
    }

    //Listener for preference changes. When sorting preference changes, this will update the recycler view with a new sorted list
    var prefsChangedListener = SharedPreferences.OnSharedPreferenceChangeListener{
        _, key ->
        if (key == "sort_preference") {
            this.songList = DataSource(adapterContext).getSongInfo()
            notifyDataSetChanged()
        }
    }
    //Register the preference changes
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        PreferenceManager.getDefaultSharedPreferences(recyclerView.context).registerOnSharedPreferenceChangeListener(prefsChangedListener)
    }


}

