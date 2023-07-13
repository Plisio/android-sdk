package net.plisio.sdk.demo

import android.app.Application
import net.plisio.sdk.PlisioClient

class PlisioSDKDemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PlisioClient.configure(
            enableLogging = true,
            showErrorDetails = true
        )
    }
}