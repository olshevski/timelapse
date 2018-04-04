package me.olshevski.timelapse;

import android.content.Context;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;

@SuppressWarnings("WeakerAccess")
public class EnableAccessibilityPreference extends Preference {

    private boolean accessibilityEnabled = false;

    public EnableAccessibilityPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.btn_enable_accessibility);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        View button = holder.findViewById(R.id.btn_enable_accessibility);
        button.setVisibility(accessibilityEnabled ? View.GONE : View.VISIBLE);
        button.setOnClickListener(v -> getOnPreferenceClickListener().onPreferenceClick(
                EnableAccessibilityPreference.this));
    }

    public void setAccessibilityEnabled(boolean enabled) {
        this.accessibilityEnabled = enabled;
        notifyChanged();
    }

}
