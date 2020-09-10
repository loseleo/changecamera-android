package com.beige.camera.common.dagger.module.support;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author：zhangqiang
 * Date：2019/1/5 16:06:23
 * Email:852286406@share_qq.com
 * Github:https://github.com/holleQiang
 */
public class ErrorInterceptor implements Interceptor {

    public static final int CODE_OUT_LOGIN = 401;

    public interface ErrorListener{

        void onUnknownError(String url, Throwable e);

        void onHttpError(String url, int code, String message);
    }

    private ErrorListener errorListener;

    public ErrorInterceptor(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        try {
            Response response = chain.proceed(request);
//            if (response.code() == CODE_OUT_LOGIN) {
//                UserInfoManager.updateUserInfo(null);
//                LoginObservables.get().publishChangedToLogoutEvent();
//                LoginUtils.goLoginPage(BaseApplication.getsInstance().getApplicationContext(),"api","api");
//            }
            if (errorListener == null) {
                return response;
            }
            if (!response.isSuccessful()) {
                errorListener.onHttpError(url,response.code(),response.message());
            }
            return response;
        } catch (Throwable e) {
            errorListener.onUnknownError(url,e);
            throw e;
        }
    }


}
