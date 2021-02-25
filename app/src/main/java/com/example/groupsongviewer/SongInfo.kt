package com.example.groupsongviewer

//Data class definition
data class SongInfo(val title: String, val artist: String, val structure: String, val choruses: List<String>, val verses: List<String>)

//Get all of the titles
fun getTitles(sList: Array<SongInfo>): Array<String> {
    val tList = mutableListOf<String>()
    for (item in sList) {
        tList.add(item.title)
    }
    return tList.toTypedArray()
}

//Get all of the artists
fun getArtists(sList: Array<SongInfo>): Array<String> {
    val aList = mutableListOf<String>()
    for (item in sList) {
        aList.add(item.artist)
    }
    return aList.toTypedArray()
}

//Get all of the song structures
fun getStructures(sList: Array<SongInfo>): Array<String> {
    val stList = mutableListOf<String>()
    for (item in sList) {
        stList.add(item.structure)
    }
    return stList.toTypedArray()
}