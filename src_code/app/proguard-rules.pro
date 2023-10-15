# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class * implements fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keepclassmembers class androidx.viewbinding.R {
#   *;
#}

-keep class * implements androidx.viewbinding.ViewBinding {
    public static *** bind(android.view.View); public static *** inflate(android.view.LayoutInflater);
}

# The following rules are used to strip any non essential Google Play Services classes and method.
-keep class com.google.ads.mediation.admob.AdMobAdapter {
    *;
}

-keep class com.google.ads.mediation.AdUrlAdapter {
    *;
}
# For Google Play Services
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
#-keep class * extends java.util.ListResourceBundle {
#   protected object[][] getContents();
#}

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

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile