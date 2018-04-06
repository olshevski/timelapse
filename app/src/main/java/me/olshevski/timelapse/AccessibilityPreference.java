package me.olshevski.timelapse;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

@SuppressWarnings("WeakerAccess")
public class AccessibilityPreference extends Preference {

    private final String summaryOn;
    private final String summaryOff;

    private boolean accessibilityEnabled = false;

    public AccessibilityPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.btn_enable_accessibility);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.AccessibilityPreference);
        summaryOn = a.getString(R.styleable.AccessibilityPreference_summaryOn);
        summaryOff = a.getString(R.styleable.AccessibilityPreference_summaryOff);
        a.recycle();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        syncButtonView(holder);
        syncSummaryView(holder);
    }

    private void syncButtonView(PreferenceViewHolder holder) {
        View button = holder.findViewById(R.id.btn_enable_accessibility);
        button.setVisibility(accessibilityEnabled ? View.GONE : View.VISIBLE);
        button.setOnClickListener(v -> getOnPreferenceClickListener().onPreferenceClick(
                AccessibilityPreference.this));
    }

    /**
     * Sync a summary holder contained within holder's subhierarchy with the correct summary text.
     *
     * @param holder PreferenceViewHolder which holds a reference to the summary view
     */
    private void syncSummaryView(PreferenceViewHolder holder) {
        // Sync the summary holder
        View view = holder.findViewById(android.R.id.summary);
        syncSummaryView(view);
    }

    private void syncSummaryView(View view) {
        if (!(view instanceof TextView)) {
            return;
        }
        TextView summaryView = (TextView) view;
        boolean useDefaultSummary = true;
        if (accessibilityEnabled && !TextUtils.isEmpty(summaryOn)) {
            summaryView.setText(summaryOn);
            useDefaultSummary = false;
        } else if (!accessibilityEnabled && !TextUtils.isEmpty(summaryOff)) {
            summaryView.setText(summaryOff);
            useDefaultSummary = false;
        }
        if (useDefaultSummary) {
            final CharSequence summary = getSummary();
            if (!TextUtils.isEmpty(summary)) {
                summaryView.setText(summary);
                useDefaultSummary = false;
            }
        }
        int newVisibility = View.GONE;
        if (!useDefaultSummary) {
            // Someone has written to it
            newVisibility = View.VISIBLE;
        }
        if (newVisibility != summaryView.getVisibility()) {
            summaryView.setVisibility(newVisibility);
        }
    }

    public void setAccessibilityEnabled(boolean enabled) {
        this.accessibilityEnabled = enabled;
        notifyChanged();
    }

}
