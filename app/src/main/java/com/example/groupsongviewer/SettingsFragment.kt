package com.example.groupsongviewer

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

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
            val prefs = PreferenceManager.getDefaultSharedPreferences(this.context)
            Toast.makeText(this.context, "Preferences Reset", Toast.LENGTH_SHORT).show()
            prefs.edit().clear().commit()
            //Reload the fragment
            val sfm = requireActivity().supportFragmentManager
            sfm.beginTransaction().replace(R.id.settings_fragment,SettingsFragment()).commit()
            //RETURN VALUES IN LAMBDA FUNCTIONS YA GOOF
            true
        }
    }


}