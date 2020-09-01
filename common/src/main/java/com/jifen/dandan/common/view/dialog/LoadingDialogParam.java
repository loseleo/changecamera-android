package com.jifen.dandan.common.view.dialog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author：zhangqiang
 * Date：2018/12/10 13:32:21
 * Email:852286406@share_qq.com
 * Github:https://github.com/holleQiang
 */
public class LoadingDialogParam implements Parcelable {

    private String message;
    private boolean cancelable;
    private boolean hasBackground;

    public LoadingDialogParam() {
    }

    public LoadingDialogParam(boolean hasBackground) {
        this.hasBackground = hasBackground;
    }

    public boolean hasBackground() {
        return hasBackground;
    }

    public void setHasBackground(boolean hasBackground) {
        this.hasBackground = hasBackground;
    }

    public String getMessage() {
        return message;
    }

    public LoadingDialogParam setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public LoadingDialogParam setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeByte(this.cancelable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasBackground ? (byte) 1 : (byte) 0);
    }

    protected LoadingDialogParam(Parcel in) {
        this.message = in.readString();
        this.cancelable = in.readByte() != 0;
        this.hasBackground = in.readByte() != 0;
    }

    public static final Creator<LoadingDialogParam> CREATOR = new Creator<LoadingDialogParam>() {
        @Override
        public LoadingDialogParam createFromParcel(Parcel source) {
            return new LoadingDialogParam(source);
        }

        @Override
        public LoadingDialogParam[] newArray(int size) {
            return new LoadingDialogParam[size];
        }
    };
}
