package com.beige.camera.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.beige.camera.BuildConfig;
import com.beige.camera.MyApplication;
import com.beige.camera.R;
import com.beige.camera.bean.FunctionBean;
import com.beige.camera.common.base.BaseActivity;
import com.beige.camera.common.router.AppNavigator;
import com.beige.camera.common.router.PageIdentity;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.MmkvUtil;
import com.beige.camera.common.utils.PermissionPageUtils;
import com.beige.camera.utils.AdHelper;
import com.beige.camera.utils.FileUtil;
import com.beige.camera.view.CustomCameraPreview;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 拍照界面
 */
@Route(path = PageIdentity.APP_CAMERA)
public class CameraActivity extends BaseActivity implements View.OnClickListener {

    public static final  String FUNCTION_IMG_TOOL = "function_img_tool";
    public static final int RESULT_LOAD_CODE = 10001;
    private CustomCameraPreview customCameraPreview;
    private ImageView cameraFlash;
    private ImageView ivLocalImage;
    private ImageView ivSwitchCamera;
    private View cropView;
    private AdHelper adHelper;

    @Autowired(name = "function")
    String function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        adHelper = new AdHelper();
    }

    @Override
    protected void setupActivityComponent() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_camera;
    }

    @Override
    public void initViews() {

        customCameraPreview = findViewById(R.id.camera_surface);
        cameraFlash = findViewById(R.id.camera_flash);
        ivLocalImage = findViewById(R.id.iv_local_image);
        ivSwitchCamera = findViewById(R.id.iv_switch_camera);
        cropView = findViewById(R.id.camera_crop_container);
        customCameraPreview.setOnClickListener(this);
        ivLocalImage.setOnClickListener(this);
        ivSwitchCamera.setOnClickListener(this);
        cameraFlash.setOnClickListener(this);
        findViewById(R.id.camera_close).setOnClickListener(this);
        findViewById(R.id.camera_take).setOnClickListener(this);
        customCameraPreview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isFinishing()){
                    customCameraPreview.focus();
                }
            }
        }, 500);


    }

    @Override
    public void initData() {

    }

    @Override
    public void configViews() {

    }

    @Nullable
    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_surface:
                customCameraPreview.focus();
                break;
            case R.id.camera_close:
                finshActivity();
                break;
            case R.id.camera_take:
                takePhoto();
                break;
            case R.id.camera_flash:
                if (customCameraPreview.switchFlashLight()) {
                    cameraFlash.setImageResource(R.mipmap.ic_flash_on);
                } else {
                    cameraFlash.setImageResource(R.mipmap.ic_flash_off);
                }
                break;
            case R.id.iv_local_image:
                selecterLocalImage();
                break;
            case R.id.iv_switch_camera:
                customCameraPreview.switchCamera();
                break;
        }
    }

    private void takePhoto() {
        MyApplication.getInstance().needShowSplashAd = false;
        customCameraPreview.setEnabled(false);
        customCameraPreview.takePhoto(new Camera.PictureCallback() {
            public void onPictureTaken(final byte[] data, final Camera camera) {
                //子线程处理图片，防止ANR
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = null;
                        if (data != null) {
                            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            camera.stopPreview();
                        }
                        if (bitmap != null) {
//                            //计算裁剪位置
                            //计算裁剪位置
                            float left = ((float) cropView.getLeft() - (float) customCameraPreview.getLeft()) / (float) customCameraPreview.getWidth();
                            float top = (float) cropView.getTop() / (float) customCameraPreview.getHeight();
                            float right = (float) cropView.getRight() / (float) customCameraPreview.getWidth();
                            float bottom = (float) cropView.getBottom() / (float) customCameraPreview.getHeight();

                            Matrix matrix = new Matrix();
                            if (customCameraPreview.currentCameraType == customCameraPreview.FRONT) {
                                matrix.postRotate(180);
                            }else{
                                matrix.postRotate(0);
                            }
                            //裁剪及保存到文件
                            Bitmap resBitmap = Bitmap.createBitmap(bitmap,
                                    (int) (left * (float) bitmap.getWidth()),
                                    (int) (top * (float) bitmap.getHeight()),
                                    (int) ((right - left) * (float) bitmap.getWidth()),
                                    (int) ((bottom - top) * (float) bitmap.getHeight()),matrix,true);

                            String imgPath = FileUtil.saveBitmap(resBitmap);
                            if (!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                            if (!bitmap.isRecycled()) {
                                resBitmap.recycle();
                            }
                            goNextActivity(imgPath);
                        }

                        return;
                    }
                }).start();
            }
        });
    }

    private void goNextActivity(String imgPath) {

        if(TextUtils.isEmpty(function)){
            AppNavigator.goImgPreviewActivity(CameraActivity.this, imgPath);
        }else if(TextUtils.equals(FUNCTION_IMG_TOOL,function)){
            Intent intent = new Intent();
            intent.putExtra("imagePathVS", imgPath);
            setResult(RESULT_OK, intent);
        }else if(TextUtils.equals(FunctionBean.ID_DETECTION_BABY,function) ||TextUtils.equals(FunctionBean.ID_DETECTION_VS,function)){
            AppNavigator.goTwoImgUploadActivity(CameraActivity.this, imgPath,function);
        }else{
            AppNavigator.goImgUploadActivity(CameraActivity.this, imgPath,function);
        }
        finish();
    }


    // 从相册中获取
    public void selecterLocalImage() {
        // 调用android的图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        CameraActivity.this.startActivityForResult(intent, RESULT_LOAD_CODE);
        MyApplication.getInstance().needShowSplashAd = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK ) {
            if (requestCode == RESULT_LOAD_CODE && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                final String picturePath = cursor.getString(columnIndex);
                cursor.close();
                goNextActivity(picturePath);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finshActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void finshActivity(){

        adHelper.playFullScreenVideoAd(this, AdHelper.getFullScreenVideoAdTypeById("CAMERA_ACTIVITY"), new AdHelper.PlayRewardedAdCallback() {
            @Override
            public void onDismissed(int action) {
                finish();
            }

            @Override
            public void onFail() {
                finish();
            }
        });
    }

}
