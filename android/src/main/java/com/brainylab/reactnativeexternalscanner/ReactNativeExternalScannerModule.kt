package com.brainylab.reactnativeexternalscanner

import android.content.Context
import android.content.res.Configuration
import android.view.InputDevice
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

class ReactNativeExternalScannerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "ReactNativeExternalScannerModule"
    }

    @ReactMethod
    fun hasExternalKeyboard(promise: Promise) {
       val isExternal = ExternalScannerUtil.hasExternalScanner(reactApplicationContext)

       if(isExternal) {
          promise.resolve(true)
       } else {
          promise.resolve(false)
       }
    }
}
