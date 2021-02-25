package com.example.groupsongviewer

//Data class definition
data class SongInfo(val title: String, val artist: String)

//Get all of the titles
fun getTitles(sList: Array<SongInfo>): Array<String> {
    var tList = mutableListOf<String>()
    if (sList != null) {
        for (item in sList) {
            tList.add(item.title)
        }
    }
    return tList.toTypedArray()
}

//Get all of the artists
fun getArtists(sList: Array<SongInfo>): Array<String> {
    var aList = mutableListOf<String>()
    if (sList != null) {
        for (item in sList) {
            aList.add(item.artist)
        }
    }
    return aList.toTypedArray()
}