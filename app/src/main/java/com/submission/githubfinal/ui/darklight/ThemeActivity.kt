package com.submission.githubfinal.ui.darklight

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.submission.githubfinal.R
import com.submission.githubfinal.databinding.ActivityThemeBinding


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class ThemeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val changeMode = findViewById<SwitchMaterial>(R.id.switchMode)
        val pref = PreferencesMode.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                changeMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                changeMode.isChecked = false
            }
        }
        changeMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}