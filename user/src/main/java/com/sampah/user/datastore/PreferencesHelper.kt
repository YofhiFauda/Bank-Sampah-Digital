package com.sampah.user.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesHelper(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val PROFILE_IMAGE_PATH_KEY = stringPreferencesKey("profile_image_path")
    }

    //Image Upload Profile
    val profileImagePath: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PROFILE_IMAGE_PATH_KEY]
    }

    suspend fun saveImagePath(path: String) {
        dataStore.edit { preferences ->
            preferences[PROFILE_IMAGE_PATH_KEY] = path
        }
    }
}

// Extension property to initialize DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pref")
