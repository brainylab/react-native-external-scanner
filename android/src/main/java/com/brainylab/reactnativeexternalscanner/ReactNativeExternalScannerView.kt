package com.brainylab.reactnativeexternalscanner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.KeyEvent
import android.view.InputDevice
import android.view.InputDevice.SOURCE_KEYBOARD
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.uimanager.UIManagerHelper

class ReactNativeExternalScannerView(context: Context) : ViewGroup(context) {
  private var codeScanned: StringBuilder = StringBuilder()
  private var childView: View? = null
  private var hasExternalKeyboard: Boolean = false
  private var mContext: ReactApplicationContext? = null
  var activeInterceptor: Boolean = true

  fun setChildView(view: View) {
      if (childView != null) {
          removeView(childView)
      }
      childView = view
      addView(view)
  }

  fun setChildParams(params: ViewGroup.LayoutParams) {
      childView?.layoutParams = params
      requestLayout()
  }


  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        childView?.let { child ->
            val centerX = (right - left) / 2
            val centerY = (bottom - top) / 2

            val childLeft = centerX - child.measuredWidth / 2
            val childTop = centerY - child.measuredHeight / 2

            val childRight = childLeft + child.measuredWidth
            val childBottom = childTop + child.measuredHeight

            child.layout(childLeft, childTop, childRight, childBottom)
        }
  }

  fun checkForExternalKeyboard(mContext: ReactApplicationContext) {
        val isExternal = ExternalScannerUtil.hasExternalScanner(mContext)

        hasExternalKeyboard = isExternal;
  }

  private fun getEventDispatcherForReactTag(reactContext: ReactContext, tag: Int): EventDispatcher? {
    return UIManagerHelper.getEventDispatcherForReactTag(reactContext, tag)
  }


  init {
    isFocusableInTouchMode = true
    requestFocus()

    setOnKeyListener { _, keyCode, event ->
            if (!hasExternalKeyboard) return@setOnKeyListener false

            val reactContext = context as ReactContext

            if (event.action == KeyEvent.ACTION_DOWN && activeInterceptor) {
                when (keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        val eventDispatcherValue = getEventDispatcherForReactTag(reactContext, id)
                        eventDispatcherValue?.dispatchEvent(ExternalScannerViewValueEvent(id, codeScanned.toString()))
                        codeScanned = StringBuilder() // clear string
                        true
                    }
                    else -> {
                        val keyChar = event.unicodeChar.toChar()
                        val valueInString = keyChar.toString()
                        codeScanned.append(valueInString)

                        val eventDispatcherSingle = getEventDispatcherForReactTag(reactContext, id)
                        eventDispatcherSingle?.dispatchEvent(ExternalScannerViewSingleValueEvent(id, valueInString))
                        true
                    }
                }
            } else {
                false
            }
    }
  }
}
