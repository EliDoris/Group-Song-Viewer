package com.example.groupsongviewer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

@Suppress("UNUSED_PARAMETER")
class SongActivity : AppCompatActivity() {

    //Index variables for keeping track of which verse/chorus is currently displayed
    var vIndex: Int = 0
    var cIndex: Int = 0
    var vMax: Int = -1
    var cMax: Int = -1

    //Song info
    var sInfo: SongInfo = SongInfo("","","",listOf(""),listOf(""))

    //Creation function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        //Get the intent that started the activity, and get its song info object
        sInfo = intent.getSerializableExtra(EXTRA_SONGINFO) as SongInfo

        //Chorus and verse max and current indices
        vMax =  sInfo.verses.size - 1
        cMax = sInfo.choruses.size - 1

        //Initial highlight, which serves to set the text initially
        highlightStructure()

        //Set verse header and text
        val verseHeaderText = findViewById<TextView>(R.id.verse_header_text)
        val verseText = findViewById<TextView>(R.id.verse_text)
        //Check values
        if (vMax < 0) {
            //No verses, remove views
            (verseHeaderText.parent as ViewGroup).removeView(verseHeaderText)
            (verseText.parent as ViewGroup).removeView(verseText)
            removeVerseButtons()
        } else {
            //At least one verse
            if (vMax > 0) {
                //More than one verse
                setMultipleVerseInfo()
            } else if (vMax == 0) {
                //Exactly one verse. Redundant check, but safer than a bare else
                removeVerseButtons()
                verseText.text = sInfo.verses[vIndex]
            }
        }

        //Set chorus header and text
        val chorusHeaderText = findViewById<TextView>(R.id.chorus_header_text)
        val chorusText = findViewById<TextView>(R.id.chorus_text)
        //Check values
        if (cMax < 0) {
            //No choruses, remove views
            (chorusHeaderText.parent as ViewGroup).removeView(chorusHeaderText)
            (chorusText.parent as ViewGroup).removeView(chorusText)
            removeChorusButtons()
        } else {
            if (cMax > 0) {
                //More than one chorus
                setMultipleChorusInfo()
            } else if (cMax == 0) {
                //Exactly one chorus. Redundant check, but safer than a bare else
                removeChorusButtons()
                chorusText.text = sInfo.choruses[cIndex]
            }
        }

    }

    //Functions for incrementing/decrementing verses
    fun incrementVerse(view: View) {
        //Invalid condition checking
        if (vMax <= 0) {
            Toast.makeText(this, "No chorus to increment", Toast.LENGTH_SHORT).show()
            return
        }
        //Increment if maximum is not already reached
        if (vIndex < vMax) {
            vIndex++
            setMultipleVerseInfo()
        }
        //Re-do the highlighting
        highlightStructure()
    }
    fun decrementVerse(view: View) {
        //Invalid condition checking
        if (vMax <= 0) {
            Toast.makeText(this, "No chorus to decrement", Toast.LENGTH_SHORT).show()
            return
        }
        //Decrement if minimum is not already reached
        if (vIndex > 0) {
            vIndex--
            setMultipleVerseInfo()
        }
        //Re-do the highlighting
        highlightStructure()
    }
    fun incrementChorus(view: View) {
        //Invalid condition checking
        if (cMax <= 0) {
            Toast.makeText(this, "No chorus to increment", Toast.LENGTH_SHORT).show()
            return
        }
        //Increment if maximum is not already reached
        if (cIndex < cMax) {
            cIndex++
            setMultipleChorusInfo()
        }
        //Re-do the highlighting
        highlightStructure()
    }
    fun decrementChorus(view: View) {
        //Invalid condition checking
        if (cMax <= 0) {
            Toast.makeText(this, "No chorus to decrement", Toast.LENGTH_SHORT).show()
            return
        }
        //Decrement if minimum is not already reached
        if (cIndex > 0) {
            cIndex--
            setMultipleChorusInfo()
        }
        //Re-do the highlighting
        highlightStructure()
    }

    //Helper functions for setting verse/chorus info
    private fun setMultipleVerseInfo() {
        val verseHeaderText = findViewById<TextView>(R.id.verse_header_text)
        val temp = "Verse " + (vIndex + 1).toString() + ":"
        verseHeaderText.text = temp
        val verseText = findViewById<TextView>(R.id.verse_text)
        verseText.text = sInfo.verses[vIndex]
    }
    private fun setMultipleChorusInfo() {
        val chorusHeaderText = findViewById<TextView>(R.id.chorus_header_text)
        val temp = "Chorus " + (cIndex + 1).toString() + ":"
        chorusHeaderText.text = temp
        val chorusText = findViewById<TextView>(R.id.chorus_text)
        chorusText.text = sInfo.choruses[cIndex]
    }

    //Helper functions for removing buttons
    private fun removeVerseButtons() {
        val verseButtonLeft = findViewById<Button>(R.id.verse_button_left)
        val verseButtonRight = findViewById<Button>(R.id.verse_button_right)
        (verseButtonLeft.parent as ViewGroup).removeView(verseButtonLeft)
        (verseButtonRight.parent as ViewGroup).removeView(verseButtonRight)
    }
    private fun removeChorusButtons() {
        val chorusButtonLeft = findViewById<Button>(R.id.chorus_button_left)
        val chorusButtonRight = findViewById<Button>(R.id.chorus_button_right)
        (chorusButtonLeft.parent as ViewGroup).removeView(chorusButtonLeft)
        (chorusButtonRight.parent as ViewGroup).removeView(chorusButtonRight)
    }

    //Functions for highlighting
    private fun highlightStructure() {
        //Get the text view
        val structureText = findViewById<TextView>(R.id.structure_text)
        //Initialize a spannable string
        val sString = SpannableString(sInfo.structure)

        //Operate on verses
        //Only need to do this if there is more than one verse
        if (vMax > 0) {
            //Pattern string
            val patternStr = "V"+(vIndex+1).toString()
            //Match the pattern
            val regex = patternStr.toRegex()
            val matchResults = regex.findAll(sString.toString())
            //Do the highlighting
            for (item in matchResults) {
                sString.setSpan(BackgroundColorSpan(Color.YELLOW),item.range.first,item.range.last+1,0)
            }
        }
        //Operate on choruses
        //Only need to do this if there is more than one chorus
        if (cMax > 0) {
            //Pattern string
            val patternStr = "C"+(cIndex+1).toString()
            //Match the pattern
            val regex = patternStr.toRegex()
            val matchResults = regex.findAll(sString.toString())
            //Do the highlighting
            for (item in matchResults) {
                sString.setSpan(BackgroundColorSpan(Color.RED),item.range.first,item.range.last+1,0)
            }
        }

        //Actually set the text
        structureText.text = sString
    }
}