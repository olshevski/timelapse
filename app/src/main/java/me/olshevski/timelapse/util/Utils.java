package me.olshevski.timelapse.util;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

public final class Utils {

    private Utils() {
    }

    /**
     * Based on
     * {@link com.android.settingslib.accessibility.AccessibilityUtils#getEnabledServicesFromSettings(Context, int)}
     *
     * @see <a href="https://github
     * .com/android/platform_frameworks_base/blob/d48e0d44f6676de6fd54fd8a017332edd6a9f096
     * /packages/SettingsLib/src/com/android/settingslib/accessibility/AccessibilityUtils
     * .java#L55">AccessibilityUtils</a>
     */
    public static boolean isAccessibilityServiceEnabled(Context context,
            Class<?> accessibilityService) {
        ComponentName expectedComponentName = new ComponentName(context, accessibilityService);

        String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting == null) {
            return false;
        }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        colonSplitter.setString(enabledServicesSetting);

        while (colonSplitter.hasNext()) {
            String componentNameString = colonSplitter.next();
            ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

            if (enabledService != null && enabledService.equals(expectedComponentName)) {
                return true;
            }
        }

        return false;
    }
}
