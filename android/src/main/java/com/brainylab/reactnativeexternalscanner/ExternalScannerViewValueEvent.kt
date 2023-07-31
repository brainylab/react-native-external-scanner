package com.brainylab.reactnativeexternalscanner

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.RCTModernEventEmitter


class ExternalScannerViewValueEvent(viewId: Int, private val codeValue: String): Event<ExternalScannerViewValueEvent>(viewId) {
    override fun getEventName(): String {
        return "topOnValueScanned"
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
        val event: WritableMap = Arguments.createMap()
        event.putString("value", codeValue)
        return event
    }
}
