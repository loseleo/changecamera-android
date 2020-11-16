#----------动态生成-------------
-keep class com.beige.qukan.web.JSApiManager{ *; }
-keep class com.beige.framework.router.** { *; }
-keep class * implements com.beige.framework.router.template.ParamInjector {*;}
-keep class * implements com.beige.framework.http.old.IHttpApi {*;}

-keepclassmembers class com.beige.qukan.plugin.framework.activity.PluginInstrumentation {*;}


#-------------Web----------------
-dontwarn android.webkit.**
-keepattributes *JavascriptInterface*
-keepattributes *JavascriptApi*
-keep class * implements com.beige.qukan.web.IH5LocaleBridge { *; }
-keep class com.beige.qkbase.web.webbridge.H5LocaleBridge { *; }
#-keep class * extends com.jifen.framework.web.bridge.AbstractApiHandler { *; }
-keep class * extends com.beige.qu.open.web.bridge.AbstractApiHandler { *; }
-keep class com.beige.qukan.web.api.** { *; }
#------web------

#------KeepAlive------
-keep class com.marswin89.**{*;}
-keep class com.marswin89.** {
    <fields>;
    <methods>;
}

#-------------Gradle----------------
#xrecycleview
#-keep class com.jcodecraeer.xrecyclerview.** { *; }

#greendao
-dontwarn org.greenrobot.**
-keep class org.greenrobot.** { *; }
-dontwarn io.requery.android.sqlcipher.**
-dontwarn io.requery.android.sqlitex.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use Rx:
-dontwarn rx.**

#eventbus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#fresco
#-keep class com.facebook.** { *; }
#-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
# Do not strip any method/class that is annotated with @DoNotStrip
#-keep @com.facebook.common.internal.DoNotStrip class *
#-keepclassmembers class * {
#    @com.facebook.common.internal.DoNotStrip *;
#}
# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**

#others
-dontwarn com.readystatesoftware.**
-keep class com.readystatesoftware.** { *; }
-dontwarn bolts.**
-keep class bolts.** { *; }
-dontwarn butterknife.**
-keep class butterknife.** { *; }

#retrofit
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.-KotlinExtensions

#rxjava
-dontwarn sun.misc.**
#-keep class io.reactivex.** { *; }
#-keep class org.reactivestreams.** { *; }
-dontwarn rx.**
#-keep class rx.**{*;}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#okhttp3
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}
-keep interface com.zhy.http.**{*;}


#rxlifecycle
#-keep class com.trello.rxlifecycle2.** { *; }


#--------------View-----------------
-keep public class com.beige.** extends android.view.View
-keepclasseswithmembers class com.beige.** {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class com.beige.** {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#frameworkHttp
#-------------model----------------
-keep class com.beige.framework.http.model.APIStatus { *; }
-keep class com.beige.framework.http.model.AbsBaseResult { *; }
-keep class com.beige.framework.http.model.BaseResult { *; }
-keep class * extends com.beige.framework.http.model.AbsBaseResult { *; }
-keep class * extends com.beige.framework.http.model.BaseResult { *; }
-keep class * extends com.beige.framework.http.model.BaseResult$* { *; }
-keep class com.beige.framework.**$* {
    *;
}
-keep class com.beige.framework.core.model.** { *; }
#---api
-keep class com.beige.framework.annotation.**{*;}
-keep class com.beige.framework.compiler.**{*;}

#-------------socket----------------
-keep public class com.beige.framework.socket.procotol.** { *; }
-keep public class com.beige.framework.socket.service.** { *; }

#-------------push----------------
#推送组件本身
-keep class com.beige.push.**{*;}
-keep class com.beige.push.ChannelResolver{*;}
-keep class com.beige.framework.push.huawei.agent.**{*;}
-keep class com.beige.framework.push.huawei.HuaweiPushRevicer{*;}
-keep class com.beige.framework.push.xiaomi.MiPushReceiver{*;}
-keep class com.beige.framework.push.vivo.VivoPushMessageReceiver{*;}
## jifen keepalive
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.beige.** implements java.io.Serializable { *; }
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#小米
-keep public class * extends android.content.BroadcastReceiver
-keep class com.beige.push.XiaoMiMessageReceiver {*;}
#华为
-ignorewarning
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.gamebox.plugin.gameservice.**{*;}
-keep public class com.huawei.android.hms.agent.** extends android.app.Activity { public *; protected *; }

-keep interface com.huawei.android.hms.agent.common.INoProguard {*;}
-keep class * extends com.huawei.android.hms.agent.common.INoProguard {*;}
#个推
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }
#信鸽
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.** {* ;}
-keep class com.tencent.mid.** {* ;}
-keep public class * extends com.qq.taf.jce.JceStruct{*;}
#push

-keep class org.greenrobot.** { *; }
-keep class com.google.** { *; }
-keep class org.hamcrest.** { *; }
-dontwarn javax.**
-keep class javax.** { *; }
-dontwarn com.sun.**
-keep class com.sun.activation.** { *; }

#rxlifecycle
#-keep class com.trello.rxlifecycle2.** { *; }

-keep class com.beige.framework.router.Module_*Extends {*;}
-keep class android.app.** {*;}
-keep class android.content.** {*;}
-keep class android.databinding.** {*;}
-keep class android.os.** {*;}
-keep class android.util.** {*;}
-keep class com.android.internal.** {*;}
-keep class com.beige.framework.common.mvp.IMvpPresenter { *; }
-keepnames @com.beige.framework.annotation.Route class *
# 之前某sdk引入，导致后面warning全部忽略，这次剔除后又集体爆发，再次加入先忽略
-ignorewarning

# 趣应用
-keep class com.beige.qu.open.** { *; }
# Gson
-keep class com.google.gson.Gson {
    public <fields>;
    private <fields>;
}

-keep class com.beige.framework.core.utils.JSONUtils {
    public <fields>;
    private <fields>;
}
-keep class com.beige.framework.core.common.App {
    *** setApplicationContext(android.app.Application);
}

-keep class com.beige.qukan.utils.NativeUtils {
    *** getInnoSoInfo(java.lang.String);
}
# 混淆插件逻辑
# -keep class com.jifen.qukan.plugin.** {*;}
# -keep class android.support.v4.app.** {*;}
-keep @com.beige.framework.core.service.QkServiceDeclare public class *

# 加固
-keep class com.inno.innosdk.pb.** {
    <fields>;
    <methods>;
}

-keep class com.inno.innosdk.bean.DeviceInfo {
    <fields>;
    <methods>;
}

-keep class com.inno.innosdk.pb.InnoMain {
    *** loadInfo(android.content.Context);
}

-keep class com.beige.qukan.lib.account.model.*

# datatracker
-keep class com.beige.platform.datatracker.** {
    <fields>;
    <methods>;
}
# 百度vr
# -dontshrink
# -keepclasseswithmembernames class * { native <methods>; }
# -keep public class com.baidu.vr.**
# -keepclassmembers public class com.baidu.vr.*{ *;}
# -keep public class bdvr.tv.danmaku.ijk.media.**
# -keepclassmembers public class bdvr.tv.danmaku.ijk.media.*{ *;}
# -keep interface bdvr.tv.danmaku.ijk.media.** {
#  <methods>;
# }
# -keepclasseswithmembers class * {
#    @bdvr.tv.danmaku.ijk.media.player.annotations.AccessedByNative <fields>;
#    @bdvr.tv.danmaku.ijk.media.player.annotations.CalledByNative <methods>;
# }
# -keep public class baiduvr.**
# -keepclassmembers public class baiduvr.*{ *;}
# 自研SDK
# 百度视频播放器
# -dontshrink
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

-keep public interface  tv.danmaku.ijk.media.player.misc.IMediaDataSource {
    <fields>;
    <methods>;
}

-keep public class tv.danmaku.ijk.media.player.exceptions.IjkMediaException {
    <fields>;
    <methods>;
}

-keep class tv.danmaku.ijk.media.player.** {
    <fields>;
    <methods>;
}

-keep class com.qukan.media.player.** {
    <fields>;
    <methods>;
}
#innosecure
-keep class com.inno.innosecure.** {
    <fields>;
    <methods>;
}
# 加固
-keep class com.inno.innosdk.pb.** {
    <fields>;
    <methods>;
}

-keep class com.inno.innosdk.bean.DeviceInfo {
    <fields>;
    <methods>;
}

-keep class com.inno.innosdk.pb.InnoMain {
    *** loadInfo(android.content.Context);
}
-keep,allowoptimization,allowobfuscation class com.inno.innosdk.pb.InnoMain {
    void changeValueMap(java.util.Map);
    java.lang.String loadInfo(android.content.Context);
    java.lang.String loadTuid(android.content.Context);
}

-keep,allowoptimization,allowobfuscation class com.inno.innosecure.InnoSecureUtils {
    byte[] secureSo(android.content.Context,java.lang.String,java.lang.String);
}
# view AndroidManifest.xml #generated:1370
-keep class com.inno.innosdk.pb.MyProvider {
    <init>(...);
}
# wechatsdk
-keep class com.tencent.mm.opensdk.** {
    <fields>;
    <methods>;
}

-keep class com.tencent.wxop.** {
    <fields>;
    <methods>;
}

-keep class com.tencent.mm.sdk.** {
    <fields>;
    <methods>;
}

-keep class com.alibaba.sdk.android.** {
     <fields>;
     <methods>;
 }

 -keep class com.ut.** {
     <fields>;
     <methods>;
 }

 -keep class com.ta.** {
     <fields>;
     <methods>;
 }

-keepclasseswithmembernames class * { native <methods>; }
-keep public interface tv.danmaku.ijk.media.player.misc.IMediaDataSource {*;}
-keep public class tv.danmaku.ijk.media.player.exceptions.IjkMediaException {*;}

-keepclasseswithmembers class * {
@tv.danmaku.ijk.media.player.annotations.AccessedByNative <fields>;
@tv.danmaku.ijk.media.player.annotations.CalledByNative <methods>; }
-keep class tv.danmaku.ijk.media.player.** { *; }
-keep class com.qukan.media.player.** { *; }


#trace
#trace keep
-keep interface com.qtt.perfmonitor.trace.listeners.IMethodBeatListener{*;}
-keep class com.qtt.perfmonitor.utils.QPerfHandlerThread { *; }
-keep class com.qtt.perfmonitor.trace.util.Utils { *; }
-keep class com.qtt.perfmonitor.AppActiveDelegate { *; }
-keep class com.qtt.perfmonitor.trace.core.LooperMonitor { *; }
-keep class com.qtt.perfmonitor.trace.core.LooperMonitor$** { *; }
-keep class com.qtt.perfmonitor.trace.core.MethodBeat { *; }
-keep class com.qtt.perfmonitor.trace.core.MethodBeat$** { *; }
-keep class com.qtt.perfmonitor.trace.core.BeatLifecycle { *; }
-keep class com.qtt.perfmonitor.utils.QPerfLog$** { *; }
-keep class com.qtt.perfmonitor.utils.QPerfUtil$** { *; }
-keep class com.qtt.perfmonitor.utils.QPerfHandlerThread$**{*;}
-keep class com.lechuan.mdwz.BuildConfig {*;}
#trace

#trace-net
-keep class okhttp3.OkHttpClient$Builder{
#  final ** networkInterceptors;
   okhttp3.OkHttpClient$Builder eventListenerFactory(okhttp3.EventListener$Factory);
   ** build();
}
-keep interface okhttp3.EventListener$Factory{ *;}
#-keep class com.qtt.perfmonitor.net.okhttp3.PerfInterceptor{*;}
-keep class com.qtt.perfmonitor.net.okhttp3.PerfEventListener{*;}
#trace-net

-keep class com.beige.open.manager.JFIdentifierManager{*;}

-keep class com.beige.compontent.ICpcCommonInterface{*;}
-keep class com.beige.compontent.ICpcCommonInterface$Callback{*;}

#InnoPush#####################
-keep class com.innotech.** {*;}

-ignorewarning

-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.android.hms.agent.**{*;}
-keep class com.huawei.gamebox.plugin.gameservice.**{*;}
-keep public class com.huawei.android.hms.agent.** extends android.app.Activity { public *; protected *; }
-keep interface com.huawei.android.hms.agent.common.INoProguard {*;}
-keep class * extends com.huawei.android.hms.agent.common.INoProguard {*;}
#InnoPush#####################

#--- qapp --#
-keep class com.beige.bridge.api.** { *; }
-keep class com.beige.bridge.base.**{*;}
-keep class com.beige.bridge.function.ad.**{*;}
-keep class com.beige.bridge.function.upload.**{*;}
-dontwarn android.webkit.**
-keepattributes *JavascriptInterface*
-keepattributes *JavascriptApi*
-keep class com.beige.bridge.IBridgeProvider{*;}
-keep public class * extends com.beige.bridge.base.apimodel.AbstractApiHandler{*;}
-keep @com.beige.framework.core.service.QkServiceDeclare public class *
-keep class com.beige.framework.core.service.QkServiceDeclare{*;}
-keep class com.beige.compontent.ICpcCommonInterface{*;}
-keep class com.beige.compontent.ICpcCommonInterface$Callback{*;}
#--- qapp --#

