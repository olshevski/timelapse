package me.olshevski.timelapse;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private static final String SYSTEM_UI_PACKAGE = "com.android.systemui";
    private static final String CAMERA_ACTIVITY = "com.android.camera.activity.main.CameraActivity";
    private static final String CAMERA_BUTTON = "com.android.camera2:id/photo_video_button";

    private ClickerServiceManager clickerServiceManager;
    private TimelapseManager timelapseManager;
    private List<AccessibilityNodeInfo> button;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication application = ((MyApplication) getApplication());
        clickerServiceManager = application.getClickerServiceManager();
        timelapseManager = application.getTimelapseManager();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.v("ERASEME", accessibilityEvent.toString());

        if (SYSTEM_UI_PACKAGE.equals(accessibilityEvent.getPackageName())) {
            // ignore notifications overlay
            return;
        }

        boolean isCameraActivityRunning = CAMERA_ACTIVITY.equals(accessibilityEvent.getClassName());
        clickerServiceManager.setClickerServiceStarted(isCameraActivityRunning);
        if (isCameraActivityRunning) {
            AccessibilityNodeInfo rootNode = accessibilityEvent.getSource();
            AccessibilityNodeInfo button = rootNode.findAccessibilityNodeInfosByViewId(
                    CAMERA_BUTTON).get(0);
            timelapseManager.setCameraButton(button);
        } else {
            timelapseManager.setCameraButton(null);
        }
    }

    @Override
    public void onInterrupt() {

    }
}
