package com.adswizz.basicsample

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ad.core.adBaseManager.AdData
import com.ad.core.adBaseManager.AdEvent
import com.ad.core.adFetcher.AdRequestConnection
import com.ad.core.adManager.AdManager
import com.ad.core.adManager.AdManagerListener
import com.adswizz.core.adFetcher.AdswizzAdRequest
import com.adswizz.sdk.AdswizzSDK
import kotlinx.android.synthetic.main.activity_main.*

// Basic app shows minimal client side functionality

class MainActivity : AppCompatActivity() {

    companion object {
        const val SERVER_URL = "demo.deliveryengine.adswizz.com"
        const val ZONE_ID = "13396"
    }

    private var adManager: AdManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar?.hide()

        val window = this.window
        getWindow().decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.my_statusbar_color)

        val scroll = ScrollingMovementMethod()
        appLogs.movementMethod = scroll

        AdswizzSDK.initialize(this)

        //Build the Ad Request with the needed parameters
        AdswizzAdRequest.Builder()
            .withServer(SERVER_URL)
            .withZoneId(ZONE_ID)
            .build() { adRequest ->
                // Create the AdRequestConnection using the above adRequest
                val adRequestConnection = AdRequestConnection(adRequest)

                adRequestConnection.requestAds { adManager, error ->
                    // Handle the response from server
                    handleResponse(error, adManager)
                }

                replayButton.setOnClickListener {
                    adManager?.reset()
                    adManager = null

                    appLogs.text = ""

                    adRequestConnection.requestAds { adManager, error ->
                        handleResponse(error, adManager)
                    }
                }
            }
    }

    private fun handleResponse(
        error: Error?,
        adManager: AdManager?
    ) {
        if (error != null) {
            appLogs.addEntry("RequestError: ${error.message}")
        } else if (adManager != null) {
            this.adManager = adManager
            appLogs.addEntry("Received ad manager")
            adManager.setListener(object : AdManagerListener {
                override fun onEventReceived(adManager: AdManager, event: AdEvent) {
                    when (event.type) {
                        AdEvent.Type.State.FirstAdWillInitialize -> {
                            appLogs.addEntry("Ad - first ad will initialize")
                        }

                        AdEvent.Type.State.Initialized -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - initialized")
                        }

                        AdEvent.Type.State.Unknown -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - in unknown state")
                        }

                        AdEvent.Type.State.WillStartLoading -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - will start loading")
                        }

                        AdEvent.Type.State.DidFinishLoading -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - did finish loading")
                            adManager.play()
                        }

                        AdEvent.Type.State.DidStartPlaying -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - did start playing")
                        }

                        AdEvent.Type.State.DidResumePlaying -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - did resume playing")
                        }

                        AdEvent.Type.State.DidPausePlaying -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - did pause playing")
                        }

                        AdEvent.Type.State.DidFinishPlaying -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - did finish playing")
                        }

                        AdEvent.Type.State.AllAdsDidFinishPlaying -> {
                            appLogs.addEntry("Ad - all ads did finish playing")
                        }

                        AdEvent.Type.State.DidSkip -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - did skip playing")
                        }

                        AdEvent.Type.State.AdUpdated -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - ad updated")
                        }

                        AdEvent.Type.Position.FirstQuartile -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - first quartile")
                        }

                        AdEvent.Type.Position.Midpoint -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - midpoint")

                        }

                        AdEvent.Type.Position.ThirdQuartile -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - third quartile")

                        }

                        AdEvent.Type.Position.Complete -> {
                            appLogs.addEntry("Ad ${event.ad?.id} - complete")
                        }
                    }
                }

                override fun onEventErrorReceived(adManager: AdManager, ad: AdData?, error: Error) {
                    appLogs.addEntry("Error - ${error.message} for ad: ${ad?.id}")
                }
            })
            adManager.prepare()
        }
    }

    override fun onDestroy() {
        AdswizzSDK.cleanup()
        super.onDestroy()
    }

    fun TextView.addEntry(entry: String) {
        val strBuilder = StringBuilder(this.text)
        strBuilder.append(entry)
        strBuilder.append("\n")
        this.text = strBuilder.toString()
    }
}
