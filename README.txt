YARN CALCULATOR - Android Studio project
========================================
1. Android Studio -> File > Open -> choose the "YarnCalculator" folder (the one with settings.gradle).
2. Wait for "Gradle sync" to finish (first time it downloads Gradle 8.7 + Android Gradle Plugin 8.5.2; needs internet ONCE).
   If Studio offers "Upgrade Gradle / AGP", accepting is fine.
3. Run on a phone/emulator with the green Run button.
4. Build APK:  Build > Build Bundle(s) / APK(s) > Build APK(s)   (debug)
   Signed release:  Build > Generate Signed App Bundle / APK... or run  gradlew assembleRelease
   (release is pre-wired to app/yarncalc.keystore, same key as the APK you already installed)

Where things are
- Calculator page : app/src/main/assets/index.html   (edit this to change the UI/logic)
- Fonts (offline) : app/src/main/assets/assets/fonts/
- Android wrapper : app/src/main/java/com/yarncalc/app/MainActivity.java
- Icons / theme   : app/src/main/res/
- App has NO INTERNET permission, so it can never go online.
