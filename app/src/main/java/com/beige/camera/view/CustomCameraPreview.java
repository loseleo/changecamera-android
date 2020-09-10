package com.beige.camera.view;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.beige.camera.utils.CamParaUtil;

/**
 * Created by gxj on 2018/2/17 01:46.
 * 相机预览封装
 */
public class CustomCameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static String TAG = CustomCameraPreview.class.getName();

    private Camera mCamera;
    public boolean isOpenFlash = false;
    private long lastSwitchCameraTime;

    public static final int FRONT = 1;//前置摄像头标记
    public static final int BACK = 2;//后置摄像头标记
    public int currentCameraType = -1;//当前打开的摄像头标记

    public CustomCameraPreview(Context context) {
        super(context);
        init();
    }

    public CustomCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = openCamera(FRONT);
        if (mCamera != null) {
            startPreview(holder);
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        mCamera.stopPreview();
        startPreview(holder);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        //回收释放资源
        release();
    }

    /**
     * 预览相机
     */
    private void startPreview( SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters parameters = mCamera.getParameters();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //竖屏拍照时，需要设置旋转90度，否者看到的相机预览方向和界面方向不相同
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(90);
            } else {
                mCamera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }
            Camera.Size bestSize = CamParaUtil.getBestSize(parameters.getSupportedPreviewSizes());
            if (bestSize != null) {
                parameters.setPreviewSize(bestSize.width, bestSize.height);
                parameters.setPictureSize(bestSize.width, bestSize.height);
            } else {
                parameters.setPreviewSize(1920, 1080);
                parameters.setPictureSize(1920, 1080);
            }
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            focus();
        } catch (Exception e) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mCamera.setDisplayOrientation(90);
                    parameters.setRotation(90);
                } else {
                    mCamera.setDisplayOrientation(0);
                    parameters.setRotation(0);
                }
                mCamera.setParameters(parameters);
                mCamera.startPreview();
                focus();
            } catch (Exception e1) {
                e.printStackTrace();
                mCamera = null;
            }
        }
    }
    /**
     * 释放资源
     */
    private void release() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 对焦，在CameraActivity中触摸对焦
     */
    public void focus() {
        if (mCamera != null) {
            mCamera.autoFocus(null);
        }
    }

    /**
     * 拍摄照片
     *
     * @param pictureCallback 在pictureCallback处理拍照回调
     */
    public void takePhoto(Camera.PictureCallback pictureCallback) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, pictureCallback);
        }
    }



    public boolean switchFlashLight() {
        if (mCamera == null || currentCameraType == FRONT) {
            return isOpenFlash;
        }
        if (isOpenFlash) {
            closeFlashLight();
        }else{
            openFlashLight();
        }
        return isOpenFlash;
    }
    /**
     * 打开闪光灯
     */
    private void openFlashLight() {

        if (mCamera == null || currentCameraType == FRONT) {
            return;
        }
        Camera.Parameters parameter = mCamera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(parameter);
        isOpenFlash = true;
    }

    /**
     * 关闭闪光灯
     */
    private void closeFlashLight() {
        if (mCamera == null || currentCameraType == FRONT) {
            return;
        }
        Camera.Parameters parameter = mCamera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameter);
        isOpenFlash = false;
    }


    private Camera openCamera(int type){
        int frontIndex =-1;
        int backIndex = -1;
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for(int cameraIndex = 0; cameraIndex<cameraCount; cameraIndex++){
            Camera.getCameraInfo(cameraIndex, info);
            if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                frontIndex = cameraIndex;
            }else if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                backIndex = cameraIndex;
            }
        }
        currentCameraType = type;
        if(type == FRONT && frontIndex != -1){
            return Camera.open(frontIndex);
        }else if(type == BACK && backIndex != -1){
            return Camera.open(backIndex);
        }
        return Camera.open();
    }


    public void switchCamera() {
        long switchCameraTiem = System.currentTimeMillis();
        if (switchCameraTiem - lastSwitchCameraTime > 1000) {
            lastSwitchCameraTime = switchCameraTiem;
            mCamera.stopPreview();
            mCamera.release();
            if(currentCameraType == FRONT){
                mCamera = openCamera(BACK);
            }else if(currentCameraType == BACK){
                mCamera = openCamera(FRONT);
            }
            startPreview(getHolder());
        }
    }

}
