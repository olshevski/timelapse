package me.olshevski.timelapse;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private static final String SYSTEM_UI_PACKAGE = "com.android.systemui";
    private static final String CAMERA_ACTIVITY = "com.android.camera.activity.main.CameraActivity";
    private static final String CAMERA_BUTTON = "com.android.camera2:id/photo_video_button";

    private TimelapseManager timelapseManager;
    private AccessibilityNodeInfo rootNode;
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
                rootNode = accessibilityEvent.getSource();
            } else {
                ForegroundService.stopForeground(this);
                rootNode = null;
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void clickCameraButton() {
        if (rootNode != null) {
            List<AccessibilityNodeInfo> cameraButton =
                    rootNode.findAccessibilityNodeInfosByViewId(CAMERA_BUTTON);
            if (cameraButton.size() > 0) {
                cameraButton.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else {
                Toast.makeText(this, R.string.toast_no_camera_button, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
