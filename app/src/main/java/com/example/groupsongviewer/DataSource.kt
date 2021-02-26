package com.example.groupsongviewer

import android.content.Context
import android.content.res.AssetManager
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class DataSource(private val context: Context) {

    //NO LONGER NECESSARY
    /*
    //Function to return all assets of a particular type
    fun getTextAssets(): Array<String> {
        //Get the assets themselves
        val amg: AssetManager = context.assets
        //Convert to list
        var fList = amg.list("")?.toList()
        //Initialize output array
        var fArray: Array<String> = arrayOf("")
        //Do the filtering
        if (fList != null) {
            fList = fList.filter{x -> x.endsWith(".txt")}
            fArray = fList.toTypedArray()
        }
        //return
        return fArray
    }


    //Function to get all titles from XML files
    fun getXmlTitles(): Array<String> {
        //Get the assets
        val amg: AssetManager = context.assets
        //Convert to list
        var fList = amg.list("")?.toList()
        //Initiate the output
        var titles = mutableListOf<String>()

        //Do the filtering
        if (fList != null) {
            fList = fList.filter{x -> x.endsWith(".xml")}
            for (item in fList) {
                val inputStream: InputStream = context.assets.open(item)
                try {
                titles.add(readTitle(inputStream)) }
                catch (e: Exception) {
                    //Just move on
                }
            }
        }
        return titles.toTypedArray()
    }

    //Outer XML parse method for the title
    private val ns: String? = null
    @Throws(XmlPullParserException::class, IOException::class)
    fun readTitle(inputStream: InputStream): String {
        inputStream.use {inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return firstTitle(parser)
        }
    }

    //Function for getting the first title instance
    @Throws(IOException::class, XmlPullParserException::class)
    private fun firstTitle(parser: XmlPullParser): String {
        var returnStr = ""
        parser.require(XmlPullParser.START_TAG, ns, "song")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "title") {
                returnStr = readText(parser)
                return returnStr
            } else {
                skip(parser)
            }
        }
        return returnStr
    }
    */

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
        //Return the song list
        return sList.toTypedArray()
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