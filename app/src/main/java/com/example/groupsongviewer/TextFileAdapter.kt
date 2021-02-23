package com.example.groupsongviewer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TextFileAdapter(val fList: Array<String>) :
    RecyclerView.Adapter<TextFileAdapter.TextViewHolder>() {

    //Describes an item view and its place within the RecyclerView
    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val songTextView: TextView = itemView.findViewById(R.id.song_text)

        fun bind(word: String) {
            songTextView.text = word
        }
    }

    //Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_item, parent, false)

        return TextViewHolder(view)
    }

    //Returns size of data list
    override fun getItemCount(): Int {
        return fList.size
    }

    //Displays data at a certain position
    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.bind(fList[position])
    }
}