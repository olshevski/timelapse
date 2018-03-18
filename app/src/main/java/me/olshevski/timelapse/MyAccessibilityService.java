package me.olshevski.timelapse;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private static final String SYSTEM_UI_PACKAGE = "com.android.systemui";
    private static final String TARGET_ACTIVITY = "me.olshevski.timelapse.MainActivity";

    private ClickerServiceManager clickerServiceManager;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private List<AccessibilityNodeInfo> button;

    @Override
    public void onCreate() {
        super.onCreate();
        clickerServiceManager = ((MyApplication) getApplication()).getClickerServiceManager();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.v("ERASEME", accessibilityEvent.toString());

        if (SYSTEM_UI_PACKAGE.equals(accessibilityEvent.getPackageName())) {
            // ignore notifications overlay
            return;
        }

        boolean isTargetActivityRunning = TARGET_ACTIVITY.equals(accessibilityEvent.getClassName());
        clickerServiceManager.setClickerServiceStarted(isTargetActivityRunning);

//        if (!"com.android.camera.activity.main.CameraActivity".equals(
//                accessibilityEvent.getClassName())) {
//            return;
//        }
//
//        AccessibilityNodeInfo rootNode = accessibilityEvent.getSource();
//
//        List<AccessibilityNodeInfo> button =
//                rootNode.findAccessibilityNodeInfosByViewId(
//                        "com.android.camera2:id/photo_video_button");
//
//        this.button = button;
//
//        handler.postDelayed(clickRunnable, 2000);

    }

    private final Runnable clickRunnable = new Runnable() {
        @Override
        public void run() {
            if (button != null) {
                button.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.v("ERASEME", "click");
            } else {
                Log.v("ERASEME", "null");
            }
        }
    };

    @Override
    public void onInterrupt() {

    }
}
