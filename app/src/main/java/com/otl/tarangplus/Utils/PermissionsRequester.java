package com.otl.tarangplus.Utils;

public class PermissionsRequester {

//    public static boolean requestFileReadPermissionIfNeeded(Activity context, int requestCode) {
//        return checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context, requestCode);
//    }
//
//    public static boolean requestFileWritePermissionIfNeeded(Activity fragment, int requestCode) {
//        return checkAndRequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, fragment, requestCode);
//    }
//
//    public static boolean requestSMSPermissionIfNeeded(Activity fragment, int requestCode) {
//        return checkAndRequestPermission(Manifest.permission.READ_SMS, fragment, requestCode);
//    }
//
//    public static boolean requestSMSRecieverPermission(Activity fragment, int requestCode) {
//        return checkAndRequestPermission(Manifest.permission.RECEIVE_SMS, fragment, requestCode);
//    }
//
//    private static boolean checkAndRequestPermission(String permission, Activity fragment, int requestCode) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//
//        if (checkPermission(permission)) {
//            return true;
//        } else {
//            fragment.requestPermissions(new String[]{permission}, requestCode);
//        }
//        return false;
//    }
//
//    public static boolean isPermissionGranted(int[] grantResults) {
//        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private static boolean checkPermission(String permission) {
//        final int permissionCheck = ContextCompat.checkSelfPermission(MyApplication.getInstance(), permission);
//        return permissionCheck == PackageManager.PERMISSION_GRANTED;
//    }
//
//    public static boolean requestPhoneStateREadPermissionIfNeeded(Activity context, int requestCode) {
//        return checkAndRequestPermission(Manifest.permission.READ_PHONE_STATE, context, requestCode);
//    }
}
