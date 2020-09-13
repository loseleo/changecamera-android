package com.beige.camera.common.dagger.module.support;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.beige.camera.common.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ParamsInterceptor implements Interceptor {


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
//        if (factory == null) {
//            return chain.proceed(chain.request());
//        }
        Request oldRequest = chain.request();
        String url = oldRequest.url().toString();
        String method = oldRequest.method();
        Request.Builder builder = oldRequest.newBuilder();
        if ("POST".equalsIgnoreCase(method)) {
            RequestBody body = oldRequest.body();
            if (body instanceof FormBody) {
                FormBody formBody = (FormBody) body;
                int size = formBody.size();
                Map<String, Object> params = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    if (!TextUtils.isEmpty(formBody.value(i))) {
                        params.put(formBody.name(i), formBody.value(i));
                    }
                }
//                params.put("request_id", InnoMain.getRid(url) );//请求时间
//                byte[] encode = InnoSecureUtils.secureSo(BaseApplication.getsInstance().getApplicationContext(), JsonUtils.mapToJsonString(params));
//                String qdata = null != encode ? Base64.encodeToString(encode, Base64.NO_WRAP) : "";
//                String qdata = null != encode ? Base64.encodeToString(encode, Base64.NO_WRAP) : "";
//                String encode = JsonUtils.mapToJsonString(params);
//                String s = TextUtils.isEmpty(encode) ? "" : encode;
//                JSONObject root = new JSONObject();
//                try {
//                    root.put("qdata", s);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),  JsonUtils.mapToJsonString(params));
                builder.post(requestBody);

            } else if (body == null || body.contentType() == null && body.contentLength() <= 0) {
                //如果没有body或者body长度和类型为空，默认body成form表单
//                String formParams = factory.createFormParams(new HashMap<String, String>());
//                builder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"),
//                        formParams));
            } else if (body instanceof MultipartBody) {
//                String formParams = factory.createFormParams(new HashMap<String, String>());
//                builder.post(RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"),
//                        formParams));
            }
        } else if ("GET".equalsIgnoreCase(method)) {

        }

        return chain.proceed(builder.build());
    }

}
