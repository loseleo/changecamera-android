package com.beige.camera.advertisement.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.beige.camera.common.utils.AppUtils;
import com.beige.camera.advertisement.permission.PermissionUtils;
import com.beige.framework.core.utils.ImageUtil;
import com.beige.qukan.ui.common.MsgUtils;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.TELECOM_SERVICE;
import static android.media.RingtoneManager.TYPE_RINGTONE;

/**
 * Smile<lijian@adeaz.com>
 * 2015-1-28
 */
public class PhoneUtils {

    //黑科技：辅助View
    public static Bitmap copyView(View sourceView) {
        sourceView.setDrawingCacheEnabled(true);
        return Bitmap.createBitmap(sourceView.getDrawingCache());
    }


    /*
     * 判断是否有存储卡
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /*
     * 从uri获取图片本地路径
     */
    public static String getPathFromUri(Uri uri, Context context) {
        if (uri == null)
            return null;
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme))
            return null;
        if (!scheme.startsWith("content")) {
            return Uri.decode(uri.getEncodedPath());
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor = null;
        String imgPath = null;
        try {
            actualImageCursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (actualImageCursor == null) {
                MsgUtils.showToast(context, "没找到图片喔！", MsgUtils.Type.WARNING);
                return null;
            }
            int imageColumnIndex = actualImageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            actualImageCursor.moveToFirst();
            imgPath = actualImageCursor.getString(imageColumnIndex);
            if (TextUtils.isEmpty(imgPath))
                return null;
            imgPath = Uri.decode(imgPath);
            if (ImageUtil.readPictureDegree(imgPath) != 0) {
                imgPath = ImageUtil.rotateBitmap(imgPath);
            }
        } catch (Exception e) {

        } finally {
            if (actualImageCursor != null) {
                actualImageCursor.close();
            }
        }
        return imgPath;
    }

    public static String getFilePathFromUri(Uri uri, Context context) {
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme)) {
            return null;
        }
        if (!scheme.startsWith("content")) {
            return Uri.decode(uri.getEncodedPath());
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor = null;
        String filePath = null;
        try {
            actualImageCursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (actualImageCursor == null) {
                MsgUtils.showToast(context, "没找到文件喔！", MsgUtils.Type.WARNING);
            } else {
                int imageColumnIndex = actualImageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                actualImageCursor.moveToFirst();
                filePath = actualImageCursor.getString(imageColumnIndex);
                filePath = Uri.decode(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (actualImageCursor != null) {
                actualImageCursor.close();
            }
        }


        return filePath;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            String[] fileList = file.list();
            if (fileList == null || fileList.length <= 0)
                return;
            for (String aFileList : fileList) {
                File delFile = new File(path + "/" + aFileList);
                if (delFile.isDirectory()) {
                    deleteFile(path + "/" + aFileList);
                } else {
                    delFile.delete();
                }
            }
        } else {
            file.delete();
        }
    }

    public static void openGallery(Activity activity, int reqCode) {
        // 激活系统图库，选择一张图片
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (!PermissionUtils.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtils.requestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionUtils.PERMISSION_WRITE_SDCARD);
            return;
        }
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), reqCode);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static File openCamera(Activity activity, int reqCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> permissions = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissions.size() > 0) {
                ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), 9999);
                return null;
            }
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File tempFile = null;
        if (PhoneUtils.hasSdcard()) {
            Uri uri = null;
            // TODO: 2020/9/2
          /*  tempFile = new File(Constants.getPathImageTemp(), "temp.jpg");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                //7.0以下
                uri = Uri.fromFile(tempFile);
            } else {
                uri = FileProvider.getUriForFile(activity,
                        activity.getPackageName() + QrBaseFileProvider.AUTHORITY_PATH, tempFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }*/

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        activity.startActivityForResult(intent, reqCode);
        return tempFile;
    }

    public static void copyToClipboard(Context context, String text) {
        if (TextUtils.isEmpty(text))
            text = "";
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("msg", text));
    }

    /**
     * 调起系统发短信功能
     *
     * @param message 短信内容
     */
    public static void sendSMS(Context context, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
        intent.putExtra("sms_body", message);
//            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//            sendIntent.putExtra("sms_body", message);
//            sendIntent.setType("vnd.android-dir/mms-sms");
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            MsgUtils.showToast(AppUtils.getAppContext(), "打不开", MsgUtils.Type.WARNING);
        }
    }

    public static void sendSMS(Context context, String number, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + number));
        intent.putExtra("sms_body", message);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            MsgUtils.showToast(AppUtils.getAppContext(), "打不开", MsgUtils.Type.WARNING);
        }
    }

    // 获取mcc+mnc
    public static String getMCCMNC(ContextWrapper context) {
        if (context == null)
            return "";
        TelephonyManager telManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operator = "";
        try {
            operator = telManager.getNetworkOperator();
        } catch (Throwable t) {
            //https://bugly.qq.com/v2/crash-reporting/crashes/f7c8d68f19/71123430/report?pid=1&crashDataType=undefined
        }
        return operator;
    }

    public static String getPathByUri(Context context, Uri data) {
        String filename = null;
        if (data.getScheme().compareTo("content") == 0) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(data, new String[]{"_data"}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    filename = cursor.getString(0);
                }
            } catch (Exception e) {

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        } else if (data.getScheme().toString().compareTo("file") == 0) {// file:///开头的uri
            filename = data.toString().replace("file://", "");// 替换file://
            if (!filename.startsWith("/mnt")) {// 加上"/mnt"头
                filename += "/mnt";
            }
        }
        return filename;
    }

    @SuppressLint("NewApi")
    public static String getPathByUri4kitkat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Android M 及以上检查是否是系统默认电话应用
     */
    public static boolean isDefaultPhoneCallApp(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TelecomManager manger = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
            if (manger != null && manger.getDefaultDialerPackage() != null) {
                return manger.getDefaultDialerPackage().equals(context.getPackageName());
            }
        }
        return true;
    }

//    public static boolean needSetDefaultPhoneCallApp(Context context) {
//       return !isDefaultPhoneCallApp(context) && !OppoUtils.isA57() && !HuaweiUtils.isAl10();
//    }

    public static String getContactName(Context context, String number) {
        if (TextUtils.isEmpty(number)) {
            return null;
        }
        final ContentResolver resolver = context.getContentResolver();

        Uri lookupUri = null;
        String[] projection = new String[]{ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = null;
        try {
            lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            cursor = resolver.query(lookupUri, projection, null, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                lookupUri = Uri.withAppendedPath(android.provider.Contacts.Phones.CONTENT_FILTER_URL,
                        Uri.encode(number));
                cursor = resolver.query(lookupUri, projection, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String ret = number;
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            ret = cursor.getString(1);
            cursor.close();
        }
        return ret;
    }

    public static void showInputMethod(EditText editText) {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 300);
    }

    public static void hintInputMethod(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 设置为默认应用
     */
//    public static void setDefaultDialerApp(Context context) {
//        // 发起将本应用设为默认电话应用的请求，仅支持 Android M 及以上
//        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && QKServiceManager.get(IAbSwitchService.class).isInSpecialAb(IAbSwitchService.KEY_PHONE_APP)) {
//            if (!isDefaultPhoneCallApp(context)) {
//                Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
//                intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME,
//                        context.getPackageName());
//                try {
//                    context.startActivity(intent);
//                } catch (Exception e) {
//                    ReportUtils.onEvent(Constants.PAGE_RING_RECOMMEND, "setting_ring_default_app",
//                            MapUtils.init()
//                                    .put("status", "fail_after_confirm")
//                                    .put("context", "setting_ring")
//                                    .build());
//                    if (e != null && e.toString().contains("FLAG_ACTIVITY_NEW_TASK")) {
//                        try {
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);
//                        } catch (Exception e1) {
//                            ReportUtils.onEvent(Constants.PAGE_RING_RECOMMEND, "setting_ring_default_app",
//                                    MapUtils.init()
//                                            .put("status", "fail_after_confirm")
//                                            .put("context", "setting_ring")
//                                            .build());
//                            e1.printStackTrace();
//                        }
//
//                    }
//
//
//                }
//            }
//        }
//    }


    /**
     * 获取音量
     *
     * @param context
     */
    public static void getVolumeStream(Context context) {
        Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context, TYPE_RINGTONE);
        Log.e("PhoneCallService", "uri  " + uri.toString());
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

//通话音量
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        Log.e("PhoneCallService", "VIOCE_CALL max : " + max + " current : " + current);


        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Log.e("PhoneCallService", "SYSTEM max : " + max + " current : " + current);

//铃声音量

        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
        Log.e("PhoneCallService", "RING max : " + max + " current : " + current);

//音乐音量

        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.e("PhoneCallService", "MUSIC max : " + max + " current : " + current);

//提示声音音量

        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        Log.e("PhoneCallService", "ALARM max : " + max + " current : " + current);
    }

}
