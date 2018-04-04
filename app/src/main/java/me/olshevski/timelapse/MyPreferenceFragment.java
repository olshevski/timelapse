package me.olshevski.timelapse;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.preference.PreferenceFragmentCompat;

import me.olshevski.timelapse.util.Utils;

public class MyPreferenceFragment extends PreferenceFragmentCompat {

    private EnableAccessibilityPreference enableAccessibilityPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        enableAccessibilityPreference = (EnableAccessibilityPreference) findPreference(
                getString(R.string.key_enable_accessibility));
        enableAccessibilityPreference.setOnPreferenceClickListener(preference -> {
            openAccessibilitySettings();
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
        enableAccessibilityPreference.setAccessibilityEnabled(isServiceEnabled);
        enableAccessibilityPreference.setSummary(
                isServiceEnabled ? R.string.preference_accessibility_summary_enabled
                        : R.string.preference_accessibility_summary_disabled);
    }

}
