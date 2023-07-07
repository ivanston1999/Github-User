package com.submission.githubfinal.ui.darklight

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PreferencesMode private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val THEME = booleanPreferencesKey("theme_setting")

        @Volatile
        private var INSTANCE: PreferencesMode? = null
        fun getInstance(dataStore: DataStore<Preferences>): PreferencesMode {
            return INSTANCE ?: synchronized(this) {
                val instance = PreferencesMode(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }


    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME] ?: false
        }
    }



    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME] = isDarkModeActive
        }
    }
}