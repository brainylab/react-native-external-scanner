package com.brainylab.reactnativeexternalscanner

import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.ReactNativeExternalScannerViewManagerInterface
import com.facebook.react.viewmanagers.ReactNativeExternalScannerViewManagerDelegate
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.soloader.SoLoader
import com.facebook.react.common.MapBuilder

@ReactModule(name = ReactNativeExternalScannerViewManager.NAME)
class ReactNativeExternalScannerViewManager(private val mCallerContext: ReactApplicationContext) : ViewGroupManager<ReactNativeExternalScannerView>(),
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
    val externalScannerView = ReactNativeExternalScannerView(context);
    externalScannerView.checkForExternalKeyboard(mCallerContext)
    return externalScannerView;
  }

  @ReactProp(name = "active")
  override fun setActive(view: ReactNativeExternalScannerView, active: Boolean) {
    view.activeInterceptor = active
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String?, Any> {
    return MapBuilder.builder<String, Any>()
            .put("topOnValueScanned",
              MapBuilder.of("registrationName", "onCodeScanned")
            )
            .put("topOnSingleValueScanned",
              MapBuilder.of("registrationName", "onSingleCodeScanned")
            ).build()
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
