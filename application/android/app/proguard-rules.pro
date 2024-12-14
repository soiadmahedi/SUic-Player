-keepattributes Signature

-keep class com.firebase.** { *; }
-dontwarn com.fasterxml.**

-keepnames class com.firebase.** { *; }
-keepnames class com.shaded.fasterxml.jackson.** { *; }
-keepnames class org.shaded.apache.** { *; }
-keepnames class javax.servlet.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.commons.logging.impl.**

-keep class com.google.firebase.database.GenericTypeIndicator{*;}
-keepclassmembers class com.google.firebase.database.GenericTypeIndicator{*;}
-keep class * extends com.google.firebase.database.GenericTypeIndicator{*;}

