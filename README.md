# Renderscript & Android 15

Sample project for Google issue [337665155](https://issuetracker.google.com/issues/337665155) for Android 15 preview.

## Description

Apps using Renderscript **crash** with the following stacktrace when they try to use Renderscript when the targetSdk is set to VanillaIceCream:

```
java.lang.UnsupportedOperationException: ScriptC scripts are not supported when targeting an API Level >= 36. Please refer to https://developer.android.com/guide/topics/renderscript/migration-guide for proposed alternatives.
    at android.renderscript.ScriptC.throwExceptionIfScriptCUnsupported(ScriptC.java:123)
    at android.renderscript.ScriptC.internalStringCreate(ScriptC.java:165)
    at android.renderscript.ScriptC.<init>(ScriptC.java:96)
```

Note how it says API level **36**, while VanillaIceCream is expected to be **35**.

## Steps to reproduce

1. Run app
2. Hit the "invert image" button

Expected result: image is inverted

Actual result: app crashes

Now, in `app/build.gradle.kts`, change `targetSdkPreview "VanillaIceCream"` to `targetSdk 34`.

Then run the above steps again: app does not crash but properly inverts the image.
