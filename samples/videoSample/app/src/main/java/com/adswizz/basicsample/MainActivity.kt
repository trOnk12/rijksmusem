package com.adswizz.basicsample

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.TypefaceSpan
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
import com.adswizz.core.adFetcher.AdswizzAdZone
import com.adswizz.sdk.AdswizzSDK
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

// Basic app shows minimal client side functionality

class MainActivity : AppCompatActivity() {

    companion object {
        const val SERVER_URL = "demo.deliveryengine.adswizz.com"
        const val ZONE_ID = "13396"
        //const val ZONE_ID = "14578"
    }

    private enum class AdManagerState {
        Unknown, ReadyForPlay, Playing, Paused, AllAdsCompleted
    }

    private var adManager: AdManager? = null
    private var adRequestConnection: AdRequestConnection? = null

    private var adManagerState: AdManagerState = AdManagerState.Unknown

    private val listener = object : AdManagerListener {
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

                AdEvent.Type.State.PreparingForPlay -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - preparing for play")
                }

                AdEvent.Type.State.ReadyForPlay -> {
                    adManagerState = AdManagerState.ReadyForPlay
                    appLogs.addEntry("Ad ${event.ad?.id} - ready for play")
                }

                AdEvent.Type.State.WillStartBuffering -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - will start buffering")
                }

                AdEvent.Type.State.DidFinishBuffering -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - did finish buffering")
                }

                AdEvent.Type.State.DidStartPlaying -> {
                    adManagerState = AdManagerState.Playing
                    appLogs.addEntry("Ad ${event.ad?.id} - did start playing")
                }

                AdEvent.Type.State.DidResumePlaying -> {
                    adManagerState = AdManagerState.Playing
                    appLogs.addEntry("Ad ${event.ad?.id} - did resume playing")
                }

                AdEvent.Type.State.DidPausePlaying -> {
                    adManagerState = AdManagerState.Paused
                    appLogs.addEntry("Ad ${event.ad?.id} - did pause playing")
                }

                AdEvent.Type.State.DidFinishPlaying -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - did finish playing")
                }

                AdEvent.Type.State.Completed -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - completed")
                }

                AdEvent.Type.State.AllAdsCompleted -> {
                    adManagerState = AdManagerState.AllAdsCompleted
                    playButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp)
                    appLogs.addEntry("Ad - all ads completed")
                }

                AdEvent.Type.State.DidSkip -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - did skip playing")
                }

                AdEvent.Type.State.NotUsed -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - not used for playing")
                }

                AdEvent.Type.State.AdUpdated -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - ad updated")
                }

                AdEvent.Type.Position.Start -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - 0% start")
                }

                AdEvent.Type.Position.FirstQuartile -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - 25% first quartile")
                }

                AdEvent.Type.Position.Midpoint -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - 50% midpoint")

                }

                AdEvent.Type.Position.ThirdQuartile -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - 75% third quartile")

                }

                AdEvent.Type.Position.Complete -> {
                    appLogs.addEntry("Ad ${event.ad?.id} - 100% complete")
                }
            }
        }

        override fun onEventErrorReceived(adManager: AdManager, ad: AdData?, error: Error) {
            appLogs.addEntry("Error - ${error.message} for ad: ${ad?.id}")
        }
    }

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

        sdkVersion.text = String.format(getString(R.string.adswizz_version_prefix, com.adswizz.sdk.BuildConfig.VERSION_NAME))

        //Build the Ad Request with the needed parameters
        AdswizzAdRequest.Builder()
                .withServer(SERVER_URL)
                .withZones(setOf(AdswizzAdZone(ZONE_ID)))
                .build() { adRequest ->
                    // Create the AdRequestConnection using the above adRequest
                    adRequestConnection = AdRequestConnection(adRequest)
                    adRequestConnection?.requestAds { adManager, error ->
                        handleResponse(error, adManager)
                    }
                }

        playButton.setOnClickListener {

            when (adManagerState) {
                AdManagerState.Unknown, AdManagerState.ReadyForPlay -> {
                    if (adManagerState == AdManagerState.Unknown) {
                        appLogs.addEntry("Recommendation: just to be sure that the ad playback will start immediately wait for the <<AdEvent.Type.State.ReadyForPlay>> event")
                    }
                    it.setBackgroundResource(R.drawable.ic_pause_arrow_black_24dp)
                    adManager?.play()
                }
                AdManagerState.Paused -> {
                    it.setBackgroundResource(R.drawable.ic_pause_arrow_black_24dp)
                    adManager?.resume()
                }
                AdManagerState.Playing -> {
                    it.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp)
                    adManager?.pause()
                }
                AdManagerState.AllAdsCompleted -> {
                    it.setBackgroundResource(R.drawable.ic_pause_arrow_black_24dp)
                    adManager?.reset()
                    adManager?.prepare()
                    adManager?.play()
                }
            }
        }

        replayButton.setOnClickListener {
            adManager?.reset()
            adManager = null
            adManagerState = AdManagerState.Unknown

            appLogs.text = ""

            adRequestConnection?.requestAds { adManager, error ->
                handleResponse(error, adManager)
            }
        }

    }

    private fun handleResponse(
            error: Error?,
            adManager: AdManager?
    ) {
        if (error != null) {
            appLogs.addEntry("RequestError: ${error.message}")
        }
        if (adManager != null) {
            this.adManager = adManager
            appLogs.addEntry("Received ad manager")
            adManager.setListener(listener)
            adManager.prepare()
        }
    }

    override fun onDestroy() {
        AdswizzSDK.cleanup()
        super.onDestroy()
    }

    fun TextView.addEntry(entry: String) {
        val ssBuilder = SpannableStringBuilder(this.text)

        val timeStamp: String = SimpleDateFormat("HH:mm:ss.SSS").format(Date())
        val entryLine = "$timeStamp $entry\n"

        ssBuilder.append(entryLine)

        val spanStart = ssBuilder.length - entryLine.length
        val spanEnd = spanStart + timeStamp.length
        ssBuilder.setSpan(RelativeSizeSpan(0.7f), spanStart, spanEnd, 0)
        ssBuilder.setSpan(ForegroundColorSpan(Color.DKGRAY), spanStart, spanEnd, 0)
        ssBuilder.setSpan(TypefaceSpan("sans-serif-condensed"), spanStart, spanEnd, 0)

        this.text = ssBuilder
    }
}
