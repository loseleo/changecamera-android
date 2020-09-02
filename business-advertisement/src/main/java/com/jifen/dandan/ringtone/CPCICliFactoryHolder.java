package com.jifen.dandan.ringtone;

import android.content.Context;

import com.iclicash.advlib.core.ICliFactory;
import com.jifen.dandan.common.utils.PackageUtils;

public class CPCICliFactoryHolder {


    private static final CPCICliFactoryHolder instance = new CPCICliFactoryHolder();
    private Context mContext;
    private ICliFactory iCliFactory;

    private CPCICliFactoryHolder() {
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    private String getVersionName(Context context) {
        String versionName = PackageUtils.getVersionName(context);
        return versionName;
    }

    public static CPCICliFactoryHolder getInstance() {
        return instance;
    }

    public ICliFactory getICliFactory() {
        if (iCliFactory == null) {
            synchronized (this) {
                if (iCliFactory == null) {
                    if (mContext == null) {
                        throw new NullPointerException("init must be called");
                    }
                    iCliFactory = ICliFactory.obtainInstance(mContext, getVersionName(mContext));
                }
            }
        }
        return iCliFactory;
    }
}
