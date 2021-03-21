package com.example.groupsongviewer

import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import android.util.Xml
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.preference.PreferenceManager
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.util.*

class DataSource(private val context: Context) {

    //Get an array of song infos
    fun getSongInfo(): Array<SongInfo> {
        //Initialize the output
        val sList = mutableListOf<SongInfo>()


        //Need the preference associated with source and how it should be sorted
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val sourcePref = prefs.getString("source_preference","source_internal")

        //Internal source
        if (sourcePref == "source_internal" || sourcePref == "source_both") {
            //Get a list of files
            val amg: AssetManager = context.assets
            val internalList = amg.list("")!!.toList().filter{it.endsWith(".xml")}
            //Parse and add0
            for (item in internalList) {
                //Create an input stream and try to read
                val internalFileStream: InputStream = context.assets.open(item)
                try {
                    sList.add(parseSongInfo(internalFileStream))
                } catch (e: Exception) {
                    //Do nothing
                }
            }
        }

        //External source
        if (sourcePref == "source_external" || sourcePref == "source_both") {
            //Need content resolver
            val contentResolver = context.contentResolver
            //File location
            val externalLocStr = prefs.getString("external_song_file_location","/")
            //Only try if it's not null or default
            if (externalLocStr != null && externalLocStr != "/") {
                try {
                    //Convert string to URI
                    val externalLocUri = Uri.parse(externalLocStr)
                    //Get children URI
                    val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(externalLocUri,DocumentsContract.getTreeDocumentId(externalLocUri))
                    //Get a cursor
                    val cursor: Cursor? = contentResolver.query(childrenUri,
                        arrayOf(DocumentsContract.Document.COLUMN_DOCUMENT_ID),null, null, null)
                    //Count number of results
                    when (cursor?.count) {
                        null -> {
                            Log.e(null, "Query error") //Error
                        } else -> {
                            //Iterate over available files
                            while (cursor.moveToNext()) {
                                //Document ID
                                val docID = cursor.getString(0)
                                //Toast.makeText(context,docID,Toast.LENGTH_LONG).show()
                                //Build a URI from that if it's an XML file
                                if (docID.endsWith(".xml")) {
                                    val docUri = DocumentsContract.buildDocumentUriUsingTree(externalLocUri,docID)
                                    //Open input stream and try to get the info from the file
                                    val externalFileStream: InputStream = contentResolver.openInputStream(docUri)!!
                                    try {
                                        sList.add(parseSongInfo(externalFileStream))
                                    } catch (e: Exception) {
                                        //Do nothing
                                    }
                                }
                            }
                        }
                    }
                    //Close the cursor
                    cursor?.close()
                } catch (e: Exception) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        //Return the song list according to preferences
        //val prefs = PreferenceManager.getDefaultSharedPreferences(this.context) // Defined above
        val sortMode = prefs.getString("sort_preference","0")?.toInt()
        return sortSongInfo(sList.toTypedArray(), sortMode!!)
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