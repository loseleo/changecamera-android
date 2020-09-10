package com.beige.camera.common.api.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UploadTokenModel implements Serializable {


    @SerializedName("access_key_id")
    private String accessKeyId;
    @SerializedName("access_key_secret")
    private String accessKeySecret;
    private long expiration;
    @SerializedName("security_token")
    private String securityToken;
    @SerializedName("bucket_name")
    private String bucketName;
    @SerializedName("end_point")
    private String endPoint;
//        @SerializedName("file_name")
//        private String fileName;
    @SerializedName("path")
    private String fileDomain;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getFileDomain() {
        return fileDomain;
    }

    public void setFileDomain(String fileDomain) {
        this.fileDomain = fileDomain;
    }



}
