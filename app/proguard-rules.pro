# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\user\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
  public *;
}

## Nineolddroid related classes to ignore
-dontwarn com.nineoldandroids.animation.**
-keep class com.nineoldandroids.animation.** { *; }
-keep interface com.nineoldandroids.animation.** { *; }
-keep class com.nineoldandroids.view.** { *; }
-keep interface com.nineoldandroids.view.** { *; }

## For Google Play Services
-dontwarn com.google.android.gms.**
-keep public class com.google.android.gms.ads.**{
   public *;
}

# For old ads classes
-keep public class com.google.ads.**{
   public *;
}

# For mediation
-keepattributes *Annotation*

# Other required classes for Google Play Services
# Read more at http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
   @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
   public static final ** CREATOR;
}

##retrofit

-dontwarn rx.**
-dontwarn retrofit.**
-dontwarn okio.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# fresco
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**

# Youtube Player
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


##################################################################################
#
# App Compat
# http://stackoverflow.com/questions/22441366/note-android-support-v4-text-icucompatics-cant-find-dynamically-referenced-cl
#
##################################################################################
-dontnote android.support.**
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-dontwarn android.support.v7.**
# -keep class android.support.v7.** { *; }  # <-- excess notes
# -keep interface android.support.v7.** { *; }
# Allow obfuscation of android.support.v7.internal.view.menu.**
# to avoid problem on Samsung 4.2.2 devices with appcompat v21
# see https://code.google.com/p/android/issues/detail?id=78377
-keep class !android.support.v7.internal.view.menu.**,android.support.** {*;}  # <-- important

# Aviary sdk
-dontwarn com.squareup.okhttp.internal.**
-dontwarn com.aviary.android.feather.sdk.AviaryIntent.**
# common library
-keep class com.aviary.android.feather.sdk.AviaryIntent
-keep class com.aviary.android.feather.sdk.internal.tracking.AviaryTracker
-keep class com.aviary.android.feather.sdk.internal.tracking.AbstractTracker
-keep class com.aviary.android.feather.sdk.log.LoggerFactory
-keep class com.aviary.android.feather.sdk.internal.headless.gl.GLUtils
-keep class com.aviary.android.feather.sdk.internal.services.BaseContextService
-keep class com.aviary.android.feather.sdk.internal.tracking.TrackerFactory


# headless library
-dontwarn com.aviary.android.feather.**
-keep interface com.aviary.android.feather.sdk.internal.headless.filters.IFilter
-keep class com.aviary.android.feather.sdk.internal.headless.AviaryEffect
-keep class com.aviary.android.feather.sdk.internal.headless.moa.Moa
-keep class com.aviary.android.feather.sdk.internal.headless.moa.MoaHD
-keep class com.aviary.android.feather.sdk.internal.headless.moa.MoaParameter
-keep class com.aviary.android.feather.sdk.internal.headless.utils.CameraUtils
-keep class com.aviary.android.feather.sdk.internal.headless.moa.MoaJavaUndo
-keep class com.aviary.android.feather.sdk.internal.headless.moa.MoaJavaUndo$MoaUndoBitmap

-keep class com.aviary.android.feather.sdk.BuildConfig
-keep class com.aviary.android.feather.cds.BuildConfig
-keep class com.aviary.android.feather.headless.BuildConfig
-keep class com.aviary.android.feather.common.BuildConfig

-keep class * extends com.aviary.android.feather.sdk.internal.headless.filters.IFilter
-keep class * extends com.aviary.android.feather.sdk.internal.headless.moa.MoaParameter

-keep class * extends com.aviary.android.feather.sdk.widget.AviaryStoreWrapperAbstract
-keep class * extends com.aviary.android.feather.sdk.widget.PackDetailLayout
-keep class * extends com.aviary.android.feather.sdk.internal.services.BaseContextService
-keep class * extends com.aviary.android.feather.sdk.internal.tracking.AbstractTracker
-keep class * extends android.app.Service
-keep class * extends android.os.AsyncTask
-keep class * extends android.app.Activity
-keep class * extends android.app.Application
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider
-keep class com.android.vending.licensing.ILicensingService
-keep public class com.android.vending.billing.IInAppBillingService
-keep class com.aviary.android.feather.sdk.internal.headless.moa.MoaResult
-keep class com.aviary.android.feather.sdk.internal.headless.filters.NativeFilterProxy
-keep class com.aviary.android.feather.sdk.utils.AviaryIntentConfigurationValidator
-keep class com.aviary.android.feather.sdk.internal.Constants
-keep class com.aviary.android.feather.sdk.AviaryIntent
-keep class com.aviary.android.feather.sdk.AviaryIntent$Builder
-keep class com.aviary.android.feather.sdk.AviaryVersion

-keepclassmembers class com.aviary.android.feather.sdk.overlays.UndoRedoOverlay {
    void setAlpha1(int);
    void setAlpha2(int);
    void setAlpha3(int);
    int getAlpha1();
    int getAlpha2();
    int getAlpha3();
}

-keepclassmembers class * extends com.aviary.android.feather.sdk.internal.graphics.drawable.FeatherDrawable {
	float getScaleX();
	void setScaleX(float);
}

-keepclassmembers class com.aviary.android.feather.sdk.AviaryIntent {*;}
-keepclassmembers class com.aviary.android.feather.sdk.AviaryIntent$Builder {*;}
-keepclassmembers class com.aviary.android.feather.sdk.AviaryVersion {*;}
-keepclassmembers class com.aviary.android.feather.sdk.utils.AviaryIntentConfigurationValidator {*;}
-keepclassmembers class com.aviary.android.feather.sdk.internal.graphics.drawable.FeatherDrawable {*;}
-keepclassmembers class com.aviary.android.feather.sdk.internal.utils.SDKUtils {*;}
-keepclassmembers class com.aviary.android.feather.sdk.internal.utils.SDKUtils$ApiKeyReader {*;}

# keep everything for native methods/fields
-keepclassmembers class com.aviary.android.feather.sdk.internal.headless.moa.Moa {*;}
-keepclassmembers class com.aviary.android.feather.sdk.internal.headless.moa.MoaHD {*;}
-keepclassmembers class com.aviary.android.feather.sdk.internal.headless.moa.MoaJavaUndo {*;}
-keepclassmembers class com.aviary.android.feather.sdk.internal.headless.utils.CameraUtils {*;}
-keepclassmembers class com.aviary.android.feather.sdk.internal.headless.moa.MoaResult {*;}
-keepclassmembers class com.aviary.android.feather.sdk.opengl.AviaryGLSurfaceView {*;}

-keepclassmembers class com.aviary.android.feather.sdk.internal.headless.filters.MoaJavaToolStrokeResult {
  <methods>;
}

-keepclassmembers class com.aviary.android.feather.sdk.internal.headless.gl.GLUtils {
  <methods>;
}

-keepclassmembers class com.aviary.android.feather.sdk.internal.headless.filters.NativeToolFilter {*;}

-keepclassmembers class com.aviary.android.feather.sdk.AviaryIntent {*;}
-keepclassmembers class com.aviary.android.feather.sdk.internal.os.AviaryIntentService {*;}
-keepclassmembers class com.aviary.android.feather.sdk.internal.os.AviaryAsyncTask {*;}

-keepclassmembers class com.aviary.android.feather.sdk.internal.tracking.AbstractTracker {
    <fields>;
}
-keepclassmembers class com.aviary.android.feather.sdk.internal.tracking.AviaryTracker {
    <fields>;
}

-keepclassmembers class com.aviary.android.feather.sdk.log.LoggerFactory {
    <fields>;
}

-keepclassmembers class com.aviary.android.feather.sdk.internal.headless.moa.MoaJavaUndo$MoaUndoBitmap {
    <fields>;
}

-keepclassmembers class com.aviary.android.feather.sdk.BuildConfig {*;}
-keepclassmembers class com.aviary.android.feather.cds.BuildConfig {*;}
-keepclassmembers class com.aviary.android.feather.headless.BuildConfig {*;}
-keepclassmembers class com.aviary.android.feather.common.BuildConfig {*;}


# keep class members
-keepclassmembers class com.aviary.android.feather.sdk.internal.tracking.AbstractTracker { *; }
-keepclassmembers class com.aviary.android.feather.sdk.internal.tracking.TrackerFactory { *; }
-keepclassmembers class com.aviary.android.feather.sdk.internal.headless.gl.GLUtils { *; }
-keepclassmembers class com.aviary.android.feather.sdk.internal.services.BaseContextService { *; }
-keepclassmembers class com.aviary.android.feather.utils.SettingsUtils { *; }

-keepclassmembers class * extends com.aviary.android.feather.sdk.internal.services.BaseContextService {
   public <init>( com.aviary.android.feather.sdk.internal.services.IAviaryController );
}

-keepclasseswithmembers class * {
    public <init>( com.aviary.android.feather.sdk.internal.services.IAviaryController );
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# Keep all the native methods
-keepclassmembers class * {
   private native <methods>;
   public native <methods>;
   protected native <methods>;
   public static native <methods>;
   private static native <methods>;
   static native <methods>;
   native <methods>;
}

-keepclasseswithmembers class * {
    public <init>( com.aviary.android.feather.sdk.internal.services.IAviaryController );
}

# EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}

#
# facebook sdk specific entries
#

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    !static !transient <fields>;
}

-keepclassmembers class com.facebook.Session {*;}
-keepattributes Signature
-keep class com.facebook.model.** { *; }

-keep class com.google.android.gms.R$styleable
-keepclassmembers class com.google.android.gms.R$styleable {*;}

