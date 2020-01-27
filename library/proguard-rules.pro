# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keep public class com.nekolaboratory.Lilium.LiliumConfig {
   public void setSafetyNetAttestReportRequestPackageName(...);
   public void setSafetyNetAttestReportRequestUserId(...);
   public void setSafetyNetAttestReportRequestVer(...);
   public void setSafetyNetAttestReportRequestAtn(...);
   public void setSafetyNetAttestReportRequestAtnError(...);
   public void setSafetyNetAttestReportRequestAtnErrorMsg(...);
   public void setSafetyNetParameterResponseApiKey(...);
   public void setSafetyNetParameterResponseNonce(...);
   public void setSafetyNetParameterRequestPackageName(...);
   public void setSafetyNetParameterRequestUserId(...);
}
-keep public class com.nekolaboratory.Lilium.Lilium {
   public void attest(...);
   public void attestWithConfig(...);
}
-keep public class com.nekolaboratory.Lilium.DefaultAttestCallback {
   public void onResult(...);
}
-keep public class com.nekolaboratory.Lilium.InstantAttestCallback {
   public void onSuccess(...);
   public void onFailed(...);
}
# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
