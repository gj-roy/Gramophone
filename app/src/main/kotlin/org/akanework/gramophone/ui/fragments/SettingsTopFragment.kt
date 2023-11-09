package org.akanework.gramophone.ui.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.akanework.gramophone.BuildConfig
import org.akanework.gramophone.R
import org.akanework.gramophone.logic.utils.ColorUtils
import org.akanework.gramophone.ui.MainActivity

class SettingsTopFragment : BasePreferenceFragment(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_top, rootKey)
        val versionPrefs = findPreference<Preference>("app_version")
        val releaseType = findPreference<Preference>("package_type")
        versionPrefs!!.summary = BuildConfig.VERSION_NAME
        releaseType!!.summary = BuildConfig.RELEASE_TYPE
    }

    override fun setDivider(divider: Drawable?) {
        super.setDivider(ColorDrawable(Color.TRANSPARENT))
    }

    override fun setDividerHeight(height: Int) {
        super.setDividerHeight(0)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == "app_name") {
            val rootView = MaterialAlertDialogBuilder(requireContext())
                .setView(R.layout.dialog_about)
                .show()
            val versionTextView = rootView.findViewById<TextView>(R.id.version)!!
            versionTextView.text =
                BuildConfig.VERSION_NAME
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "theme_mode" -> {
                when (sharedPreferences?.getString("theme_mode", "0")) {
                    "0" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                    "1" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    "2" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
            }
            "amoled" -> {
                if (sharedPreferences?.getBoolean("amoled", false) == true) {
                    ColorUtils.overrideAmoledColor = true
                    requireActivity().recreate()
                } else {
                    ColorUtils.overrideAmoledColor = false
                    requireActivity().recreate()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

}