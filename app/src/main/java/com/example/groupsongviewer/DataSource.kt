package com.example.groupsongviewer

import android.content.Context
import android.content.res.AssetManager
import android.util.Xml
import android.widget.Toast
import androidx.preference.PreferenceManager
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class DataSource(private val context: Context) {

    //Get an array of song infos
    fun getSongInfo(): Array<SongInfo> {
        //Initialize the output
        val sList = mutableListOf<SongInfo>()

        //Get the assets and convert to list form
        val amg: AssetManager = context.assets
        var fList = amg.list("")?.toList()

        //Filter and parse
        if (fList != null) {
            //Filter
            fList = fList.filter{x -> x.endsWith(".xml")}
            //Parse
            for (item in fList) {
                val inputStream: InputStream = context.assets.open(item)
                try {
                    sList.add(parseSongInfo(inputStream)) }
                catch (e: Exception) {
                    //Just move on
                }
            }
        }
        //Return the song list according to preferences
        val prefs = PreferenceManager.getDefaultSharedPreferences(this.context)
        var sortMode = prefs.getString("sort_preference","0")?.toInt()
        if (sortMode == null) {
            sortMode = 0
        }
        return sortSongInfo(sList.toTypedArray(),sortMode)
    }

    //Sort functionality. Sorts according to the modality requested
    private fun sortSongInfo(sList: Array<SongInfo>, mode: Int): Array<SongInfo> {
        when (mode) {
            0 -> sList.sortBy{ ignoreThe(it.title).toLowerCase(Locale.ROOT)} // Sort by title
            1 -> sList.sortBy{ ignoreThe(it.artist).toLowerCase(Locale.ROOT)} // Sort by artist
        }
        return sList
    }

    //Function for ignoring "The " in titles/artists
    private fun ignoreThe(inputStr: String): String {
        return if (inputStr.startsWith("the ", true)) {
            inputStr.substring(4,inputStr.length)
        } else {
            inputStr
        }
    }

    //Parse function
    private var ns: String? = null
    @Throws(XmlPullParserException::class, IOException::class)
    fun parseSongInfo(inputStream: InputStream): SongInfo {
        inputStream.use {iStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(iStream, null)
            parser.nextTag()
            return readSongInfo(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun readSongInfo(parser: XmlPullParser): SongInfo {
        //Initialize output
        var title = ""
        var artist = ""
        var structure = ""
        val choruses = mutableListOf<String>()
        val verses = mutableListOf<String>()
        //Make sure we're dealing with a song
        parser.require(XmlPullParser.START_TAG, ns, "song")
        //Iterate until EOF
        while (parser.next() != XmlPullParser.END_TAG) {
            //Do nothing on everything that isn't a start tag
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            //Read data or skip as necessary
            when (parser.name) {
                "title" -> title = readText(parser)
                "artist" -> artist = readText(parser)
                "structure" -> structure = readText(parser)
                "chorus" -> choruses.add(readText(parser))
                "verse" -> verses.add(readText(parser))
                else -> skip(parser)
            }
        }
        return SongInfo(title,artist,structure,choruses,verses)
    }

    //Function for reading text
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String{
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    //Function for skipping tags
    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}