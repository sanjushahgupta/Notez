package compose.notezz.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(private val context: Context) {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore("LoginStatus")
        val LOGIN_STATUS_KEY = stringPreferencesKey("LoginStatus")
    }

    val loginStatus: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LOGIN_STATUS_KEY] ?: "loggedOut"

    }

    suspend fun saveLoginStatus(status: String = "loggedOut") {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_STATUS_KEY] = status
        }
    }

}
