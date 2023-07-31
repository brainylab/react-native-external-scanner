package com.brainylab.reactnativeexternalscanner

import android.content.Context
import android.content.res.Configuration
import android.view.InputDevice
import android.view.KeyCharacterMap
import android.view.inputmethod.InputMethodManager
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext

object ExternalScannerUtil {
    fun isExternalScanner(inputDevice: InputDevice, resourcesConfiguration: Configuration): Boolean {
        val sources = inputDevice.sources
        if ((sources and InputDevice.SOURCE_KEYBOARD) != InputDevice.SOURCE_KEYBOARD) {
            return false // Not a keyboard device
        }

        // Check if the device has a key character map
        val keyCharacterMap = inputDevice.keyCharacterMap
        if (keyCharacterMap == null || keyCharacterMap.keyboardType == KeyCharacterMap.VIRTUAL_KEYBOARD) {
            return false // Virtual (on-screen) keyboard
        }

        // Check if the device has a physical keyboard (i.e., not a virtual keyboard)
        val hasPhysicalKeyboard = (resourcesConfiguration.keyboard != Configuration.KEYBOARD_NOKEYS)

        // If it has a physical keyboard, and it's not virtual (emulator or virtual device)
        return hasPhysicalKeyboard && !inputDevice.isVirtual
    }

    fun hasExternalScanner(reactContext: ReactApplicationContext): Boolean {
        val inputManager = reactContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val inputDevices = InputDevice.getDeviceIds()
        val resourcesConfiguration = reactContext.resources.configuration

        for (deviceId in inputDevices) {
            val device = InputDevice.getDevice(deviceId)
            if (device != null && isExternalScanner(device, resourcesConfiguration)) {
                return true
            }
        }
        return false
    }
}
