package com.example.groupsongviewer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.text.InputType
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import java.net.URI

//Identifier for return value from intent
const val EXTRA_DOCTREE = 101

class SettingsFragment : PreferenceFragmentCompat() {


    @SuppressLint("ApplySharedPref")
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        //Make sure only numbers get read in for number preferences
        val displayTextSize: EditTextPreference? = findPreference("display_text_size")
        displayTextSize?.setOnBindEditTextListener { editText -> editText.inputType = InputType.TYPE_CLASS_NUMBER }
        val firstLineMargin: EditTextPreference? = findPreference("first_line_margin")
        firstLineMargin?.setOnBindEditTextListener { editText -> editText.inputType = InputType.TYPE_CLASS_NUMBER }
        val wrappedLineMargin: EditTextPreference? = findPreference("wrapped_line_margin")
        wrappedLineMargin?.setOnBindEditTextListener { editText -> editText.inputType = InputType.TYPE_CLASS_NUMBER }

        //Reset handling
        val resetPrefButton: Preference? = findPreference("reset_preferences")
        resetPrefButton?.setOnPreferenceClickListener {
            //Load preferences and reset
            val prefs = PreferenceManager.getDefaultSharedPreferences(this.context)
            prefs.edit().clear().commit()
            //Notify the user
            Toast.makeText(this.context, "Preferences Reset", Toast.LENGTH_SHORT).show()
            //Reload the fragment
            val sfm = requireActivity().supportFragmentManager
            sfm.beginTransaction().replace(R.id.settings_fragment,SettingsFragment()).commit()
            //RETURN VALUES IN LAMBDA FUNCTIONS YA GOOF
            true
        }

        //External file location handling
        val externalLocationButton: Preference? = findPreference("external_song_file_location")
        externalLocationButton?.setOnPreferenceClickListener {
            //Load preferences and get URI from the preference string
            val prefs = PreferenceManager.getDefaultSharedPreferences(this.context)
            val uri = Uri.parse(prefs.getString("external_song_file_location","/"))
            //Do the opening of the directory
            openDirectory(uri)
            //Obligatory true return
            true
        }
        val externalLocationPreference: Preference? = findPreference("external_song_file_location")
        val prefs = PreferenceManager.getDefaultSharedPreferences(this.context)
        externalLocationPreference?.summary = Uri.decode(prefs.getString("external_song_file_location",""))

    }

    //Function for opening a document location
    private fun openDirectory(pickerInitialUri: Uri) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply{
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        startActivityForResult(intent, EXTRA_DOCTREE)
    }

    //Function for handling a received activity result. Currently only handles file location
    @SuppressLint("ApplySharedPref")
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        //Verify the right code
        if (resultData != null && requestCode == EXTRA_DOCTREE) {
            //Use the result to update preferences
            val contentResolver = this.requireContext().contentResolver
            resultData.data?.also { uri ->
                //Decode string
                val uriStr = uri.toString()
                //Set preference
                val prefs = PreferenceManager.getDefaultSharedPreferences(this.context)
                prefs.edit().putString("external_song_file_location",uriStr).commit()
                //Update summary
                val externalLocationPreference: Preference? = findPreference("external_song_file_location")
                externalLocationPreference?.summary = Uri.decode(uriStr)
                //Persist permissions
                contentResolver.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
    }

}