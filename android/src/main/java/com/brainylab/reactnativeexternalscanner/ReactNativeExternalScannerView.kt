package com.brainylab.reactnativeexternalscanner

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.EventDispatcher
import java.nio.charset.Charset

class ReactNativeExternalScannerView(context: Context) : ViewGroup(context) {
  private var codeScanned: StringBuilder = StringBuilder()
  private var childView: View? = null
  private var hasExternalKeyboard: Boolean = false
  private var mContext: ReactApplicationContext? = null
  private var keyListener: OnKeyListener? = null

  init {
    setOnKeyListener(null)

    setOnFocusChangeListener(
      OnFocusChangeListener { view, hasFocus ->
        if (!hasFocus) {
          val reactContext = context as ReactContext

          val eventDispatcher = getEventDispatcherForReactTag(reactContext, id)
          val eventData = Arguments.createMap()
          eventData.putString("value", "")

          eventDispatcher?.dispatchEvent(ExternalScannerViewEvent(id, "topOnWithoutFocus", eventData))
        }
      },
    )
  }

  fun setChildView(view: View) {
    if (childView != null) {
      removeView(childView)
    }
    childView = view
    addView(view)
  }

  override fun onLayout(
    changed: Boolean,
    left: Int,
    top: Int,
    right: Int,
    bottom: Int,
  ) {
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
    hasExternalKeyboard = isExternal
  }

  private fun getEventDispatcherForReactTag(
    reactContext: ReactContext,
    tag: Int,
  ): EventDispatcher? {
    return UIManagerHelper.getEventDispatcherForReactTag(reactContext, tag)
  }

  private fun getUTF8Char(event: KeyEvent): String? {
    val keyCode = event.keyCode
    val action = event.action
    val unicodeChar = event.unicodeChar.toChar()

    if (action == KeyEvent.ACTION_DOWN && keyCode != KeyEvent.KEYCODE_UNKNOWN) {
      if (unicodeChar != 0.toChar()) {
        val utf8Bytes = unicodeChar.toString().toByteArray(Charset.forName("UTF-8"))
        return String(utf8Bytes, Charset.forName("UTF-8"))
      }
    }
    return null
  }

  fun setViewAddFocus() {
    isFocusableInTouchMode = true
    requestFocus()

    if (keyListener == null) {
      keyListener =
        OnKeyListener { _, keyCode, event ->
          if (!hasExternalKeyboard) return@OnKeyListener false

          val reactContext = context as ReactContext

          if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
              KeyEvent.KEYCODE_ENTER -> {
                val eventDispatcherValue = getEventDispatcherForReactTag(reactContext, id)

                val eventData = Arguments.createMap()
                eventData.putString("value", codeScanned.toString())

                eventDispatcherValue?.dispatchEvent(ExternalScannerViewEvent(id, "topOnValueScanned", eventData))
                codeScanned.clear()
                true
              }
              else -> {
                val utf8Char = getUTF8Char(event)
                if (utf8Char != null) {
                  codeScanned.append(utf8Char)

                  val eventDispatcherSingle = getEventDispatcherForReactTag(reactContext, id)
                  val eventData = Arguments.createMap()
                  eventData.putString("value", utf8Char)

                  eventDispatcherSingle?.dispatchEvent(ExternalScannerViewEvent(id, "topOnSingleValueScanned", eventData))
                }
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
    isFocusableInTouchMode = false
    clearFocus()

    if (keyListener != null) {
      setOnKeyListener(null)
      keyListener = null
    }
  }
}
