package com.univreview.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.univreview.Navigator;
import com.univreview.util.PermissionsChecker;

/**
 * Created by DavidHa on 2017. 3. 12..
 */
public class PermissionCheckerActivity extends BaseActivity {
    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private PermissionsChecker checker;
    private boolean requiresCheck = true;
    private String type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucent();

        //permissions
        checker = new PermissionsChecker(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (requiresCheck && checker.lacksPermissions(PERMISSIONS)) {
            requestPermissions(PERMISSIONS);
            requiresCheck = false;
        } else if (!checker.lacksPermissions(PERMISSIONS)) {
           getPermission();
        }
    }

    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
           getPermission();
        } else {
            showMissingPermissionDialog();
        }
    }

    private void showMissingPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("도움말")
                .setMessage("상품 사진을 올리려면 저장공간 권한이 필요합니다." +
                        "\n[설정]->[권한]에서 해당권한(저장공간)을 활성화해주세요.")
                .setNegativeButton("거부", (dialog, which) -> finish())
                .setPositiveButton("설정", (dialog, which) -> {
                    requiresCheck = true;
                    Navigator.goAppSetting(this);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void getPermission(){
        Intent intent = new Intent();
        intent.putExtra("type", type);
        setResult(RESULT_OK, intent);
        finish();
    }


}
