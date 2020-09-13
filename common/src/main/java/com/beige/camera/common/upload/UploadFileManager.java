
package com.beige.camera.common.upload;

import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.beige.camera.common.api.CommonApi;
import com.beige.camera.common.api.beans.UploadTokenModel;
import com.beige.camera.common.base.BaseApplication;
import com.beige.camera.common.base.bean.ApiResult;
import com.beige.camera.common.dagger.component.CommonComponentHolder;
import com.beige.camera.common.utils.FileUtils;
import com.beige.camera.common.utils.LogUtils;
import com.beige.camera.common.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UploadFileManager {

    @Inject
    CommonApi api;
    private volatile static UploadFileManager mSingleton = null;

    private OSSClient oSSClient;
    private UploadTokenModel mUploadTokenModel;
    public interface UploadFileCallback {
        void onProgress(long currentSize, long totalSize);

        void onSuccess(String ossFilePath, String eTag, String requestId);

        void onFailure(String requestId, String errorCode, String message);
    }

    public interface GetOssTokenCallback {
        void onNext();
        void onError();
    }


    private UploadFileManager() {
        CommonComponentHolder.getCommonComponent().inject(this);
    }

    public static UploadFileManager getInstance() {
        if (mSingleton == null) {
            synchronized (UploadFileManager.class) {
                if (mSingleton == null) {
                    mSingleton = new UploadFileManager();
                }
            }
        }
        return mSingleton;
    }


    private void initOSSClient() {
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(mUploadTokenModel.getAccessKeyId(),
                mUploadTokenModel.getAccessKeySecret(), mUploadTokenModel.getSecurityToken());
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒。
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒。
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个。
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次。
        oSSClient = new OSSClient(BaseApplication.getsInstance().getApplicationContext(), mUploadTokenModel.getEndPoint(), credentialProvider);
    }


    private boolean isValidResponse() {
        if (mUploadTokenModel == null || TextUtils.isEmpty(mUploadTokenModel.getAccessKeyId())
                || TextUtils.isEmpty(mUploadTokenModel.getAccessKeySecret())
                ||  mUploadTokenModel.getExpiration() == 0
                || TextUtils.isEmpty(mUploadTokenModel.getSecurityToken())
                || TextUtils.isEmpty(mUploadTokenModel.getBucketName())
                || TextUtils.isEmpty(mUploadTokenModel.getEndPoint())
                || TextUtils.isEmpty(mUploadTokenModel.getFileDomain())) {
            return false;
        }

        long l = mUploadTokenModel.getExpiration() - System.currentTimeMillis();
        LogUtils.e("zhangning", "Expiration(): " + l );
        if (mUploadTokenModel.getExpiration() - System.currentTimeMillis()/1000 < 5 * 60 ) {
            return false;
        }
        return true;
    }

    private void getOssToken(GetOssTokenCallback getOssTokenCallback) {
        api.getUploadSignature()
                .compose(RxUtil.io_main())
                .subscribe(new Observer<ApiResult<UploadTokenModel>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ApiResult<UploadTokenModel> tokenModel) {
                        mUploadTokenModel = tokenModel.getData();
                        if(getOssTokenCallback != null){
                            getOssTokenCallback.onNext();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("zhangning", "getOssToken error:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void uploadFile(String fileType,String filePath, UploadFileCallback uploadFileCallback) {
        LogUtils.e("zhangning", "isValidResponse: " + isValidResponse());
        if (!isValidResponse()) {
            getOssToken(new GetOssTokenCallback() {
                @Override
                public void onNext() {
                    uploadFile(fileType,filePath,uploadFileCallback);
                }

                @Override
                public void onError() {
                    if(uploadFileCallback != null){
                        uploadFileCallback.onFailure("", "", "oss鉴权借口异常");
                    }
                }
            });
            return;
        }

        if (oSSClient == null) {
            initOSSClient();
        }

        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        final String fileName = mUploadTokenModel.getFileDomain() + fileType +  "/" + FileUtils.getFileNameByUrl(filePath);
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(mUploadTokenModel.getBucketName(), fileName, filePath);
        LogUtils.e("zhangning", "fileUrl: " + fileName);
        // 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                LogUtils.e("zhangning", "currentSize: " + currentSize + " totalSize: " + totalSize);
                if(uploadFileCallback != null) {
                    uploadFileCallback.onProgress(currentSize, totalSize);
                }
            }
        });

        OSSAsyncTask task = oSSClient.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                LogUtils.e("zhangning", "PutObject:UploadSuccess" + "  ETag:" + result.getETag() + "  RequestId:" + result.getRequestId());
                if(uploadFileCallback != null) {
                   String imageUrl =  "http://" +mUploadTokenModel.getBucketName() + "." + mUploadTokenModel.getEndPoint() + "/"+ fileName;
                    LogUtils.e("zhangning", "imageUrl = " + imageUrl);
                    uploadFileCallback.onSuccess(imageUrl,result.getETag(), result.getRequestId());
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                    if(uploadFileCallback != null) {
                        uploadFileCallback.onFailure("", "", "网络异常");
                    }
                }
                if (serviceException != null) {
                    if(uploadFileCallback != null) {
                        uploadFileCallback.onFailure(serviceException.getRequestId(), serviceException.getErrorCode(), serviceException.getRawMessage());
                    }
                    // 服务异常。
                    LogUtils.e("zhangning", "ErrorCode:" + serviceException.getErrorCode() + "  RequestId:" + serviceException.getRequestId() +
                            "  HostId:" + serviceException.getHostId() + "  RawMessage:" + serviceException.getRawMessage());
                }
            }
        });
// task.cancel(); // 可以取消任务。
// task.waitUntilFinished(); // 等待任务完成。
    }

}
