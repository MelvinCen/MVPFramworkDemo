package com.melvin.mvpframworkdemo.utils.permission;

import java.util.List;

/**
 * @Author Melvin
 * @CreatedDate 2017/10/31 14:30
 * @Description ${TODO}
 * @Update by       Melvin
 * @Date 2017/10/31 14:30
 * @Description ${TODO}
 */

public interface PermissionListener {

    void onGranted(int requestCode, List<String> grantedPermission);

    void onAllGranted(int requestCode, List<String> allGrantedPermission);

    void onDenied(int requestCode, List<String> deniedPermission);
}
