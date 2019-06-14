package com.example.tangwenyan.map.Util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 高德定位相关权限类
 */

public class PermissionsUtils {

    /**
     * 需要进行检测的定位权限数组
     */
    public static String[] locationPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private PermissionsUtils() {
    }

    public static boolean checkPermissions(Activity activity, int requestcode, String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions, activity);
        if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(activity, needRequestPermissonList.toArray(
                    new String[needRequestPermissonList.size()]), requestcode);
            return false;
        } else {
            return true;
        }
    }

    private static List<String> findDeniedPermissions(String[] permissions, Activity activity) {
        List<String> needRequestPermissonList = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(activity,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    public static boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
