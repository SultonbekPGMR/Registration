package com.sultonbek1547.registration.utils

import android.content.Context
import android.content.SharedPreferences


object MySharedPreference {
    private const val NAME = "cache_final"
    private const val MODE = Context.MODE_PRIVATE

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var LOG_IN_STATE: Boolean
        get() = preferences.getBoolean("state", false)
        set(value) = preferences.edit {
            if (value != null) {
                it.putBoolean("state", value)
            }
        }


}