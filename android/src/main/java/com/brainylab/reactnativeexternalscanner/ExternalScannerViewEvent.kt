package com.brainylab.reactnativeexternalscanner

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.RCTModernEventEmitter


class ExternalScannerViewEvent(viewId: Int, private val eventName: String, private val eventData: WritableMap): Event<ExternalScannerViewEvent>(viewId) {
    override fun getEventName(): String {
        return eventName
    }

    override fun dispatchModern(rctEventEmitter: RCTModernEventEmitter?) {
        super.dispatchModern(rctEventEmitter)
        rctEventEmitter?.receiveEvent(
            -1,
            viewTag, eventName,
            Arguments.createMap()
        )
    }

    override fun getEventData(): WritableMap {
      return eventData
    }
}
