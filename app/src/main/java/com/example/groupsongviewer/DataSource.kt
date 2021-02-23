package com.example.groupsongviewer

import android.content.Context
import android.content.res.AssetManager

class DataSource(val context: Context) {

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

}