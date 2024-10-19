package com.imcalculator.calculator.managers

import android.content.Context
import androidx.security.crypto.MasterKey

class SharedPreferencesManager(context: Context) {
    private val cryptoSharedPreferences = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private var preferences = androidx.security.crypto.EncryptedSharedPreferences.create(
        context,
        "calculator_app",
        cryptoSharedPreferences,
        androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private var editor: android.content.SharedPreferences.Editor  = preferences.edit()

    fun removeValue(key: String) {
        editor.remove(key.lowercase())
        editor.apply()
    }

    fun isContained(s: String?): Boolean {
        return preferences!!.contains(s)
    }

    fun <T> setValue(key: String, value: T) {
        when (value) {
            is String -> editor.putString(key.lowercase(), value)
            is Boolean -> editor.putBoolean(key.lowercase(), value)
            is Int -> editor.putInt(key.lowercase(), value)
            is Long -> editor.putLong(key.lowercase(), value)
        }
        editor.apply()
    }

   fun <T>getValue(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> preferences.getString(key.lowercase(), defaultValue) as T
            is Boolean -> preferences.getBoolean(key.lowercase(), defaultValue) as T
            is Int -> preferences.getInt(key.lowercase(), defaultValue) as T
            is Long -> preferences.getLong(key.lowercase(), defaultValue) as T
            else -> throw IllegalArgumentException("Unknown type")
        }
    }
}