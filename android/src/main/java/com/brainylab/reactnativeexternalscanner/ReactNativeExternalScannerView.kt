package com.brainylab.reactnativeexternalscanner

import android.content.Context
import android.util.AttributeSet
import android.util.Log;
import android.view.View
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.InputDevice
import android.view.View.OnFocusChangeListener
import android.view.InputDevice.SOURCE_KEYBOARD
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter


class ReactNativeExternalScannerView(context: Context) : ViewGroup(context) {
  private var codeScanned: StringBuilder = StringBuilder()
  private var childView: View? = null
  private var hasExternalKeyboard: Boolean = false
  private var mContext: ReactApplicationContext? = null
  private var keyListener: OnKeyListener? = null

  init {
    setOnKeyListener(null)

    setOnFocusChangeListener(OnFocusChangeListener { _, hasFocus ->
    Log.i("APP SPE", "not focus")
          if (!hasFocus) {
            val reactContext = context as ReactContext

            val eventDispatcher = getEventDispatcherForReactTag(reactContext, id)
            val eventData = Arguments.createMap()
            eventData.putString("value", "")

            eventDispatcher?.dispatchEvent(ExternalScannerViewEvent(id, "topOnWithoutFocus", eventData))

          }
    })
  }

  override fun onDetachedFromWindow() {
    Log.i("APP SPE", "clea detached view")
    super.onDetachedFromWindow()
    setViewClearFocus()
  }

  fun setChildView(view: View) {
      if (childView != null) {
          removeView(childView)
      }
      childView = view
      addView(view)
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

  fun setViewAddFocus() {
    Log.i("APP SPE", "view focus")
    isFocusableInTouchMode = true
    requestFocus()
    // Adicionar o OnKeyListener quando o setViewFocus for chamado
        if (keyListener == null) {
            keyListener = OnKeyListener { _, keyCode, event ->
                if (!hasExternalKeyboard) return@OnKeyListener false

                val reactContext = context as ReactContext

                if (event.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_ENTER -> {
                            val eventDispatcherValue = getEventDispatcherForReactTag(reactContext, id)

                            val eventData = Arguments.createMap()
                            eventData.putString("value", codeScanned.toString().lowercase())

                            eventDispatcherValue?.dispatchEvent(ExternalScannerViewEvent(id, "topOnValueScanned", eventData))
                            codeScanned.clear()
                            true
                        }
                        else -> {
                            val eventDispatcherSingle = getEventDispatcherForReactTag(reactContext, id)

                            val keyChar = event.unicodeChar.toChar()
                            val valueInString = keyChar.toString().lowercase()
                            codeScanned.append(valueInString)

                             val eventData = Arguments.createMap()
                            eventData.putString("value", valueInString)

                            eventDispatcherSingle?.dispatchEvent(ExternalScannerViewEvent(id, "topOnSingleValueScanned", eventData))
                            true
                        }
                    }
                } else {
                    false
                }
            }
            setOnKeyListener(keyListener)
        }
  }

  fun setViewClearFocus() {
    Log.i("APP SPE", "view clear")
    isFocusableInTouchMode = false
    clearFocus()

    // Remover o OnKeyListener quando o setClearFocus for chamado
    if (keyListener != null) {
      setOnKeyListener(null)
      keyListener = null
    }
  }
}
