#-optimizationpasses 7
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontoptimize
-dontusemixedcaseclassnames
-verbose
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
#-overloadaggressively

#------------------  下方是android平台自带的排除项，这里不要动         ----------------

-keep public class * extends android.app.Activity{
	public <fields>;
	public <methods>;
}
-keep public class * extends android.app.Application{
	public <fields>;
	public <methods>;
}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepattributes *Annotation*

-keepclasseswithmembernames class *{
	native <methods>;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclasseswithmembers class * {
    ... *JNI*(...);
}

-keepclasseswithmembernames class * {
	... *JRI*(...);
}

-keep class **JNI* {*;}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

#-optimizationpasses 5
#-dontoptimize
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-ignorewarnings
-verbose
-dontwarn
-keepattributes InnerClasses,LineNumberTable

-dontwarn **CompatHoneycomb
-dontwarn **CompatHoneycombMR2
-dontwarn **CompatCreatorHoneycombMR2

-keepclassmembers class * extends android.webkit.WebChromeClient{
   		public void openFileChooser(...);
   		public void onShowFileChooser(...);
}

-dontwarn android.support.v4.**

-keep class android.support.** { *; }
-keep interface android.support.** { *; }

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep class android.support.v4.view.**{ *;}
-keep class android.support.v4.content.**{ *;}

-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep class com.google.android.apps.analytics.PipelinedRequester$Callbacks

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keep class org.** {*;}
-keep class org.scribe.**{*;}

#gson
-keepattributes Signature

-keep class sun.misc.Unsafe { *; }

-keep class com.google.gson.examples.android.model.** { *; }

-keepclassmembers class * {
    public <init>(org.json.JSONObject);
 }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class android.support.** {*; }
-keep class org.java_websocket.** {*; }
-keep class android.os.** {*; }
-keep class com.google.gson.** { *; }
-keep class org.apache.http.** { *; }

#rxjava
-keep class retrofit2.adapter.rxjava.**{*;}
-keep interface retrofit2.adapter.rxjava.** { *; }

-keep class retrofit2.**{*;}
-keep interface retrofit2.** { *; }

-keep class rx.**{*;}
-keep interface rx.** { *; }

-keep class dagger.**{*;}
-keep interface dagger.** { *; }

-keep class org.ccil.cowan.tagsoup.**{*;}
-keep interface org.ccil.cowan.tagsoup.** { *; }

-keep class org.greenrobot.eventbus.**{*;}
-keep interface org.greenrobot.eventbus.** { *; }

-keep class com.bumptech.glide.**{*;}
-keep interface com.bumptech.glide.** { *; }

-keep class com.bumptech.glide.**{*;}
-keep interface com.bumptech.glide.** { *; }

-keep class javax.annotation.**{*;}
-keep interface javax.annotation.** { *; }

-keep class javax.inject.**{*;}
-keep interface javax.inject.** { *; }

-keep class okhttp3.**{*;}
-keep interface okhttp3.** { *; }

-keep class okio.**{*;}
-keep interface okio.** { *; }

-keep class uk.co.senab.photoview.**{*;}
-keep interface uk.co.senab.photoview.** { *; }

-keep class jp.wasabeef.glide.transformations.**{*;}
-keep interface jp.wasabeef.glide.transformations.** { *; }

-keep class android.support.design.**{*;}
-keep interface android.support.design.** { *; }

-keep class com.jifen.dandan.common.module.** { *; }
-keep class com.jifen.dandan.db.entity.** { *; }

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep class **.R{*;}
-keep class com.mob.**{*;}
-keep interface com.mob.**{*;}
-dontwarn **.R$*

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#友盟
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.jifen.dandan.R$*{
public static final int *;
}

#Trace sdk
-keep class com.qtt.perfmonitor.trace.core.MethodBeat { *; }
-keep class com.jifen.dandan.BuildConfig{
    public static final java.lang.String APPLICATION_ID;
    public static final java.lang.String VERSION_NAME;
}

-keep class com.jifen.framework.router.** {*;}
-keep class * implements com.jifen.framework.router.template.ParamInjector {*;}


# manifest: provider for updates
-keep public class com.huawei.hms.update.provider.UpdateProvider { public *; protected *; }
-keep class android.support.v4.content.FileProvider {*;}

#hotfix
-keepclassmembers class * {
    public static ** sMethodTrampoline;
}
-keep class com.jifen.qukan.patch.MethodPatchEntry{ *; }
-keep class com.jifen.qukan.patch.MethodTrampoline{ *; }

-keep class com.jifen.dandan.bean.** { *; }
-keep interface com.jifen.dandan.bean.** { *; }
-keep class com.jifen.dandan.common.base.bean.** { *; }
-keep interface com.jifen.dandan.common.base.bean.** { *; }
-keep class com.jifen.dandan.ad.AdJsApi { *; }
-keep class com.jifen.dandan.common.feed.bean.AdModel { *; }
-keep class com.jifen.dandan.ad.api.bean.** { *; }
-keep class com.jifen.dandan.common.push.bean.** { *; }
-keep class * extends com.jifen.qu.open.web.bridge.AbstractApiHandler {*;}

#广告
-keep class com.iclicash.advlib.**{*;}
#-keep class android.support.v4.*.** { *; }
#-keep class android.support.v7.*.** { *; }

#穿山甲
-keep class com.ttshell.sdk.api.**{ public *; }
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}

#穿山甲
-keep class com.bykv.vk.openvk.** { *; }
-keep public interface com.bykv.vk.openvk.downloadnew.** {*;}
-keep class com.pgl.sys.ces.* {*;}

########### 直播 startActivity #############
#if u use bugly
-keep public class com.tencent.bugly.**{*;}

# RongCloud SDK
-keep class io.rong.** {
    <fields>;
    <methods>;
}

-keep class cn.rongcloud.** {
    <fields>;
    <methods>;
}

-keep class * extends io.rong.imlib.model.MessageContent {
    <fields>;
    <methods>;
}

#plugin framework
-keep class com.jifen.qukan.plugin.framework.runtime.fragment.** {*;}
-keep class com.jifen.qukan.plugin.framework.activity.PluginInstrumentation {
    <fields>;
    <methods>;
}

#rong im
-keep class io.rong.**{*;}

#innosdk
-keep class com.inno.innosecure.**{*;}

#实现注入
-keep class com.uqu.live.sdkbridge.** {*;}
-keep class com.uqu.lib.im.** {*;}
-keep class com.jifen.qukan.plugin.framework.fake.FakePackageParseCallbackImp {*;}

########### 直播 end #############

# 畅思广告
-keep class com.com.bhb.cbgarp.** {*;}
-keep class com.android.net.** {*;}
-keep class com.android.internal.http.multipart.** {*;}
-keep class org.apache.commons.** {*;}
-dontwarn com.com.bhb.cbgarp.**
-dontwarn com.share.**
-keep class com.share.ads.** {*;}
-keep class com.share.exception.** {*;}
-dontwarn com.orhanobut.logger.**
-keep class com.orhanobut.logger.**{*;}
-keep interface com.orhanobut.logger.**{*;}

#TUIA
-allowaccessmodification
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
#保持类不被混淆
-keep class android.support.annotation.** {*;}
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class com.tuia.ad.Ad { *; }

#ugc混淆规则
-keep class com.innotech.media.** { *; }
-keep class com.android.innshortvideo.core.** { *; }
-keep class sdk.android.innshortvideo.innimageprocess.** { *; }

#提现sdk混淆
-keep class com.jifen.qu.withdraw.** { *; }


#crash report
-keeppackagenames com.jifen.qukan.**, com.jifen.qu.open.**, com.jifen.platform.**, com.jifen.open.**, com.jifen.framework.**, com.jifen.compontent.**, com.jifen.bridge.**, com.jifen.allspark.**, com.innotech.**


-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# If you use the byType method to obtain Service, add the following rules to protect the interface:
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# If single-type injection is used, that is, no interface is defined to implement IProvider, the following rules need to be added to protect the implementation
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider
#oss
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**
