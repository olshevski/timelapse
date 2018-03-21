package me.olshevski.timelapse;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityService extends AccessibilityService {

    private static final String SYSTEM_UI_PACKAGE = "com.android.systemui";
    private static final String CAMERA_ACTIVITY = "com.android.camera.activity.main.CameraActivity";
    private static final String CAMERA_BUTTON = "com.android.camera2:id/photo_video_button";

    private TimelapseManager timelapseManager;
    private AccessibilityNodeInfo cameraButton;
    private final TimelapseManager.Action cameraButtonAction = this::clickCameraButton;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication application = ((MyApplication) getApplication());
        timelapseManager = application.getTimelapseManager();
        timelapseManager.setAction(cameraButtonAction);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timelapseManager.setAction(null);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        boolean isSystemUI = SYSTEM_UI_PACKAGE.equals(accessibilityEvent.getPackageName());
        timelapseManager.setOnHold(isSystemUI);
        if (!isSystemUI) {
            boolean isCameraActivityRunning = CAMERA_ACTIVITY.equals(
                    accessibilityEvent.getClassName());
            if (isCameraActivityRunning) {
                ForegroundService.startForeground(this);
                AccessibilityNodeInfo rootNode = accessibilityEvent.getSource();
                cameraButton = rootNode.findAccessibilityNodeInfosByViewId(
                        CAMERA_BUTTON).get(0);
            } else {
                ForegroundService.stopForeground(this);
                cameraButton = null;
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void clickCameraButton() {
        if (cameraButton != null) {
            cameraButton.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

}
