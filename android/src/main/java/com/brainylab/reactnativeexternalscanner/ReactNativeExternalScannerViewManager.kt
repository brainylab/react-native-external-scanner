package com.brainylab.reactnativeexternalscanner

import android.graphics.Color
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.ReactNativeExternalScannerViewManagerInterface
import com.facebook.react.viewmanagers.ReactNativeExternalScannerViewManagerDelegate
import com.facebook.soloader.SoLoader

@ReactModule(name = ReactNativeExternalScannerViewManager.NAME)
class ReactNativeExternalScannerViewManager : SimpleViewManager<ReactNativeExternalScannerView>(),
  ReactNativeExternalScannerViewManagerInterface<ReactNativeExternalScannerView> {
  private val mDelegate: ViewManagerDelegate<ReactNativeExternalScannerView>

  init {
    mDelegate = ReactNativeExternalScannerViewManagerDelegate(this)
  }

  override fun getDelegate(): ViewManagerDelegate<ReactNativeExternalScannerView>? {
    return mDelegate
  }

  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): ReactNativeExternalScannerView {
    return ReactNativeExternalScannerView(context)
  }

  @ReactProp(name = "color")
  override fun setColor(view: ReactNativeExternalScannerView?, color: String?) {
    view?.setBackgroundColor(Color.parseColor(color))
  }

  companion object {
    const val NAME = "ReactNativeExternalScannerView"

    init {
      if (BuildConfig.CODEGEN_MODULE_REGISTRATION != null) {
        SoLoader.loadLibrary(BuildConfig.CODEGEN_MODULE_REGISTRATION)
      }
    }
  }
}
