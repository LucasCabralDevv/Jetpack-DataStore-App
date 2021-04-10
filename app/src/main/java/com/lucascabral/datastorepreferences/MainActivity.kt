package com.lucascabral.datastorepreferences

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.lucascabral.datastorepreferences.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val dataStore: DataStore<Preferences> = createDataStore("settings")
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.run {

            buttonSave.setOnClickListener {
                lifecycleScope.launch {
                    val key = inputKey.text?.toString() ?: ""
                    val value = inputValue.text?.toString() ?: ""
                    saveData(key, value)
                }
            }

            buttonRead.setOnClickListener {
                lifecycleScope.launch {
                    val readKey = inputReadKey.text?.toString() ?: ""
                    textDataStoredValue.text = readData(readKey) ?: getString(R.string.not_found_value_read, readKey)
                }
            }
        }
    }

    private suspend fun saveData(key: String, value: String) {
        val preferencesKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[preferencesKey] = value
        }
    }

    private suspend fun readData(key: String): String? {
        val preferencesKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[preferencesKey]
    }
}