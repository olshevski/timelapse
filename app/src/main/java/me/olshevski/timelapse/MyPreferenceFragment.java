package me.olshevski.timelapse;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import me.olshevski.timelapse.pref.GeneralPreference;
import me.olshevski.timelapse.util.Utils;

public class MyPreferenceFragment extends PreferenceFragmentCompat {

    /* preference generated with SharedPreferencesGenerator */
    private GeneralPreference generalPreference;

    /* preferences from android.support.v7.preference package */
    private AccessibilityPreference accessibilityPreference;
    private SwitchPreference soundsPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generalPreference = new GeneralPreference(getContext());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        accessibilityPreference = (AccessibilityPreference) findPreference(
                getString(R.string.key_accessibility));
        accessibilityPreference.setOnPreferenceClickListener(preference -> {
            openAccessibilitySettings();
            return true;
        });
        soundsPreference = (SwitchPreference) findPreference(getString(R.string.key_sounds));
        soundsPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            generalPreference.setSoundsEnabled((Boolean) newValue);
            return true;
        });
    }

    private void openAccessibilitySettings() {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    @Override
    public void onStart() {
        super.onStart();
        boolean isServiceEnabled = Utils.isAccessibilityServiceEnabled(getContext(),
                MyAccessibilityService.class);
        accessibilityPreference.setAccessibilityEnabled(isServiceEnabled);
        soundsPreference.setChecked(generalPreference.isSoundsEnabled());
    }

}
