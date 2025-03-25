package com.zendalona.mathmantra.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.Arrays;

public class PermissionManager {
    public static final int REQUEST_CODE_MICROPHONE = 100;
    public static final int REQUEST_CODE_ACCELEROMETER = 101;

    private final Activity activity;
    private final PermissionCallback callback;
    private String permissionToRequest;
    private final String TAG = "PermissionManager";

    public PermissionManager(Activity activity, PermissionCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public void requestMicrophonePermission() {
        permissionToRequest = Manifest.permission.RECORD_AUDIO;
        Log.i(TAG + " :: requestMicrophonePermission()", permissionToRequest);
        requestPermission();
    }

    public void requestAccelerometerPermission() {
        permissionToRequest = Manifest.permission.BODY_SENSORS;
        Log.i(TAG + " :: requestAccelerometerPermission()", permissionToRequest);
        requestPermission();
    }

    private int getRequestCode() {
        switch (permissionToRequest){
            case Manifest.permission.RECORD_AUDIO: return REQUEST_CODE_MICROPHONE;
            case Manifest.permission.BODY_SENSORS: return REQUEST_CODE_ACCELEROMETER;
            default: Log.e("PermissionManager", "Unknown permission: " + permissionToRequest); return -1;
        }
    }

    private void requestPermission() {
        if (permissionToRequest != null) {
            if (ContextCompat.checkSelfPermission(activity, permissionToRequest) == PackageManager.PERMISSION_GRANTED) {
                if (callback != null) callback.onPermissionGranted();
            } else {
                Log.w(TAG  + " :: requestPermission()", "Permission not already granted. " + permissionToRequest + "w/" + getRequestCode());
                String[] permissionsToRequest = {Manifest.permission.RECORD_AUDIO, Manifest.permission.BODY_SENSORS};
                Log.i(TAG, "requesting permissions : " + Arrays.toString(permissionsToRequest));
                ActivityCompat.requestPermissions(activity, permissionsToRequest, 101);
//                ActivityCompat.requestPermissions(activity, new String[]{permissionToRequest}, 42);
            }
        }
        else Log.e(TAG + " :: requestPermission()", "permissionToRequest value is null");
    }

    public void handlePermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(callback != null) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) callback.onPermissionGranted();
             else callback.onPermissionDenied();
        }
        else Log.w(TAG + " :: handlePermissionsResult() ","callback value is null");
    }

    public interface PermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
    }
}
