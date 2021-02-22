package org.techtown.omni_beta100.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionSupport {

    private Context context;
    private Activity activity;

    private String[] permissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    private List permissionList;

    private final int MULTIPLE_PERMISSIONS = 1023;

    public PermissionSupport(Activity _activity, Context _context) {
        this.activity = _activity;
        this.context = _context;

    }

    //허용 받아야할 권한이 남았는지 체크
    public boolean checkPermission() {
        int result;
        permissionList = new ArrayList<>();

        // 위에서 배열로 선언한 권한 중 허용되지 않은 권한이 있는지 체크
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(context, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            return false;
        }
        return true;
    }


    //권한 허용 요청
    public void requestPermission() {
        ActivityCompat.requestPermissions(activity, (String[]) permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
    }

    //권한 요청 대한 결과 처리

    public boolean permissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {

        if (requestCode == MULTIPLE_PERMISSIONS && (grantResults.length > 0)) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    return false;
                }
            }
        }

        return true;
    }
}
