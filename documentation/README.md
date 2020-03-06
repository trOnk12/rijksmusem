# **AdswizzSDK - Integration guide**
 * [<strong>Before you start</strong>](#before-you-start)
      * [What is ‘Client-Side Insertion’](#what-is-client-side-insertion)
      * [What is ‘Server-Side Insertion’](#what-is-server-side-insertion)
      * [Prerequisites for ‘Client-Side Insertion’](#prerequisites-for-client-side-insertion)
      * [Prerequisites for ‘Server-Side Insertion’](#prerequisites-for-server-side-insertion)
 * [<strong>Get started</strong>](#get-started)
      * [Prerequisites](#prerequisites)
      * [Dependencies](#dependencies)
      * [Permissions](#permissions)
      * [Adding the SDK to your AndroidStudioProject project](#adding-the-sdk-to-your-android-studio-project)
      * [SDK initialization and cleanup](#SDK-initialization-and-cleanup)
 * [<strong>Client-Side Insertion</strong>](#client-side-insertion)
      * [Your first ad request](#your-first-ad-request)
      * [Working with AdManager object](#working-with-admanager-object)
      * [AdManager operations](#admanager-operations)
      * [AdManager Interface](#admanager-interface)
         * [prepare](#prepare)
         * [play](#play)
         * [pause](#pause)
         * [skipAd](#skipad)
         * [reset](#reset)
 * [<strong>Server-Side Insertion</strong>](#server-side-insertion)
      * [Your first stream manager](#your-first-stream-manager)
      * [AdStreamManager Listener interface](#adstreammanager-listener-interface)
 * [<strong>Interactive ads</strong>](#interactive-ads)
      * [Handling interactive ad events](#handling-interactive-ad-events)
 * [<strong>Companion Banner</strong>](#companion-banner)
      * [Adding an AdCompanionView](#adding-an-adcompanionview)
      * [Setting up](#setting-up)
      * [Companion events](#companion-events)
      * [Extra exposure time](#extra-exposure-time)
 * [<strong>Playing ads using your player</strong>](#playing-ads-using-your-player)
      * [AdPlayer Interface](#adplayer-interface)
 * [<strong>AdswizzSDK general settings</strong>](#adswizzsdk-general-settings)
      * [GDPR consent](#gdpr-consent)
      * [CCPA config](#ccpa-config)
      * [AFR config](#afr-config)
      * [Integrator Context](#integrator-context)
      * [Extra exposure time for an AdCompanionView](#extra-exposure-time-for-an-adcompanionview)
 * [<strong>(Optional) Prepare your application for advanced targetability capabilities</strong>](#optional-prepare-your-application-for-advanced-targetability-capabilities)
      * [Privacy implications](#privacy-implications)
 * [<strong>Sample projects</strong>](#sample-projects)
      * [BasicSample](#basicsample)
      * [StreamingSample](#streamingsample)

# Before you start

This guide is addressed to the Android developers who want to integrate AdswizzSDK in their apps.
Here is a quick overview. More details will be provided for each scenario in their respective section.

## What is Client-Side Insertion

'Client-Side Insertion' represents the insertion of ads into the audio stream done by the integrator. Your app is responsible for the built-in logic to decide when to start and end an ad break by fetching ads from the ad server and playing them with your apps's audio/video capabilities. The SDK will handle all communication during ad fetching with the ad server while being in charge with the display of companion banner and performing various event reporting (impressions & quartiles).

## What is Server-Side Insertion

‘Server-Side Insertion’ represents the insertion of ads in the audio stream done by the server in real time. It requires AIS as a streaming server. AIS is the acronym for Audio Injector for Servers and is an AdsWizz product that does 'server-side insertion'.

The responsibilities are split between streaming server and SDK as follows:
* Streaming Server:
   * detects ad breaks and inserts audio ads into the audio stream
   * sends metadata information to make possible for the SDK to synchronize companion banner with the audio content and to detect interactive ads
* SDK:
   * decorates audio stream URL to increase targetability of ads
   * detects all events associated with an ad break (start, stop, change of ad)
   * retrieves, displays, synchronizes companion banner with audio content based on metadata information
   * may handle the display area for companion banners outside of an ad break
   * retrieves and process interactivity information based on metadata

## Prerequisites for ‘Client-Side Insertion’

In order to successfully do the ‘Client-Side Insertion’ you will need to set/provide the following information for your **_AdswizzAdRequest_** object within **_AdRequestConnection_**:
*  adServer = the name of AdServer used to fetch ads from
*  zoneId = identifier of zone used to retrieve audio/video ads from
*  companionZones = (optional) identifier of zone to retrieve creatives to be displayed by the companion banner
*  (optional) list of custom site variables used for ads selection (e.g referrer)

The following can be set as general AdswizzSDK parameters:
*  (optional) GDPR consent value
*  (optional) CCPA (U.S. privacy string) consent value
*  (optional) integrator context. This contain information that the integrator should provide and will be used by the SDK (for example for VAST macro expansion)

For the client-side scenario, you may use any streaming server and you are not require to use an AIS.

## Prerequisites for ‘Server-Side Insertion’

In order to successfully integrate the ‘Server-Side Insertion’, you will need to set the following information:
* in afrConfig:
   *  server = the name of Adserver used to insert ads from
   *  endpoint = contains the identifier of zone to retrieve creatives to be displayed by the companion banner
* in **_AdswizzAdStreamManager_**:
   *  adStreamURL = URL of audio stream. This is given in the play method.

The following can be set as general AdswizzSDK parameters:
*  (optional) GDPR consent value
*  (optional) CCPA (U.S. privacy string) consent value
*  (optional) integrator context. This contain information that the integrator should provide and will be used by the SDK (for example for VAST macro expansion)


# Get started

AdswizzSDK facilitates the ad lifecycle management, right from your application.
Sensing listeners environment, it crafts the overall ad exercise for superior addressability and augmented interactivity.

On top, in a client-side insertion scenario, it retrieves the ad with its associated assets, and handles the reporting operations.<br>
In a server-side insertion scenario, it takes the pressure off your app by making the interactivity and assets easily available.


## Prerequisites

* AndroidStudio 3.4+
* Gradle build system

## Dependencies
 A list of external dependencies used in our SDK:
```groovy
kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${kotlin_version}"
kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
appcompat = "androidx.appcompat:appcompat:${appcompatVersion}"
coreKtx = "androidx.core:core-ktx:${coreKtxVersion}"
firebaseAds = "com.google.firebase:firebase-ads:${firebaseAdsVersion}"
exoPlayer = "com.google.android.exoplayer:exoplayer:$exoPlayerVerison"
gmsPlayServicesAds = "com.google.android.gms:play-services-ads:$gmsPlayServicesAdsVersion"
moshi = "com.squareup.moshi:moshi:$moshiVersion"
moshiAdapters = "com.squareup.moshi:moshi-adapters:$moshiVersion"
moshiCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"
androidMaterial = "com.google.android.material:material:$androidMaterialVersion"
constraintLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
lifeCycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifeCycleExtensionsVersion"
preferenceKtx = "androidx.preference:preference-ktx:$coreKtxVersion"
firebaseAnalytics = "com.google.firebase:firebase-analytics:$firebaseVersion"
crashlyticsSdk = "com.crashlytics.sdk.android:crashlytics:$crashlyticsVersion"
kotlinxCoroutines= "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion"
kotlinxCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinxCoroutinesVersion"
```

## Permissions

With the addition of the AdswizzSDK to your project, there will be some permissions that will appear in your merged manifest file.
You don't need to do anything.

The SDK uses the following permissions:

```xml
<manifest>
.....
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.WRITE_CONTACTS" />
<uses-permission android:name="android.permission.WRITE_CALENDAR" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.VIBRATE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
......
</manifest>
```

## Adding the SDK to your Android Studio Project

You can easily integrate AdswizzSDK into your project. There are 2 steps to this integration:

1. Inside your project level build.gradle add the following block inside the allprojects->repositories tag:

```groovy
maven {
    url "https://raw.githubusercontent.com/adswizz/ad-sdk-android/master/releases"
        credentials(HttpHeaderCredentials) {
            name = "Authorization"
            value = "Bearer YOUR-AUTH-TOKEN-PROVIDED-BY-ADSWIZZ-PIM"
        }
    authentication {
        header(HttpHeaderAuthentication)
    }
}
```

The final code should look something like this:

```groovy
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://raw.githubusercontent.com/adswizz/ad-sdk-android/master/releases"
            credentials(HttpHeaderCredentials) {
                name = "Authorization"
                value = "Bearer YOUR-AUTH-TOKEN-PROVIDED-BY-ADSWIZZ-PIM"
            }
            authentication {
                header(HttpHeaderAuthentication)
            }
        }
    }
}
```

2. Inside your module level build.gradle add the following line inside your dependencies block:

```groovy
implementation 'com.adswizz:adswizz-sdk:version'
```

Where <strong>version</strong> is the latest version of the SDK provided by AdsWizz (i.e. 7.0.5)

## SDK initialization and cleanup

First, you need to add the installationId, provided by an AdsWizz engineer, to your manifest. It should look like this:

```xml
<application
    android:name="path.to.myApp.MyApp"
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">
    .......
    <meta-data android:name="com.adswizz.core.installationId" android:value="ADD_YOUR_INSTALLATION_ID_HERE" />
    .......
</application>
```

Second, you need to add the playerId to your manifest. This can have any value that you want. It should look like this:

```xml
<application
    android:name="path.to.myApp.MyApp"
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">
    .......
    <meta-data android:name="com.adswizz.core.installationId" android:value="ADD_YOUR_INSTALLATION_ID_HERE" />
    <meta-data android:name="com.adswizz.core.playerId" android:value="ADD_YOUR_PLAYER_ID_HERE" />
    .......
</application>
```
Next, you need to initialize the AdswizzSDK. The recommended way to do this is in the onCreate of your application. If you already extended
the application class just add the following line inside the onCreate method:

```kotlin
AdswizzSDK.initialize(this)
```

If you didn't already extend the Application class you can do it now. It should look something like this:

```kotlin
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AdswizzSDK.initialize(this)
    }
}
```

Don't forget to add this new class in your manifest. It should look like this:

```xml
<application
    android:name="path.to.myApp.MyApp"
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">
    .......
</application>
```

For cleanup you should use:

```kotlin
AdswizzSDK.cleanup()
```

Call it when you will no longer need the SDK.


# Client-Side Insertion

## Your first ad request

You are now ready for your first ad request. You will need to create an AdswizzAdRequest object and configure it.

```kotlin
val adRequest: AdswizzAdRequest = AdswizzAdRequest.Builder() //Build the Ad Request with the needed parameters
            .withServer("SERVER_PROVIDED_BY_PIM")
            .withZoneId("ZONEID_PROVIDED_BY_PIM")
            .build()
```
Ad server and zoneId will be provided to you by an AdsWizz PIM.

After this point you need create an AdRequestConnection object and call requestAds with the ad request object.

```kotlin            
val adRequestConnection = AdRequestConnection(adRequest) // Create the AdRequestConnection using the above adRequest

adRequestConnection.requestAds { adManager, error ->
            // Handle the response from server
            handleResponse(error, adManager)
        }
```
As a result, of this call the SDK will provide you with an error if the call was a failure or an AdManager object if the result was a success.

## Working with AdManager object

If the request to the AdsWizz Ad server was a success, the SDK will return an AdManager object which you will own and will be the way the SDK will communicate events back to your application. \
To get this communication channel open, you need to set up a listener for the AdManager that conforms to the AdManagerListener interface. The AdManager will call:

`onEventReceived(adManager: AdManager, event: AdEvent)` whenever events of interest might happen in the SDK. Consult AdEvent.Type for a list of possible events from the AdswizzSDK.

If an error happens in the SDK while using this object, `onEventErrorReceived(adManager: AdManager, ad: AdData?, error: Error)` will be called

As a first step, an **_AdManager_** needs to have some settings. You can create an **_AdManagerSettings_** object and pass it to your newly created instance of **_AdManager_**.
In this object you can specify if you want to play the ad with the SDK’s internal player or a player of your choice that must conform to **_AdPlayer_** interface.

Next, you need to call prepare method on the **_AdManager_** object.
This will buffer the ads if you decide the play them with the internal player. Here is how it looks like.

```kotlin
class MainActivity : AppCompatActivity(), AdManagerListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adRequest: AdswizzAdRequest =
            AdswizzAdRequest.Builder() //Build the Ad Request with the needed parameters
                .withServer("SERVER_PROVIDED_BY_PIM")
                .withZoneId("ZONEID_PROVIDED_BY_PIM")
                .withPlayerId("PLAYERID_PROVIDED_BY_PIM")
                .build()

        val adRequestConnection =
            AdRequestConnection(adRequest) // Create the AdRequestConnection using the above adRequest

        adRequestConnection.requestAds { adManager, error ->
            // Handle the response from server
            if (adManager != null) {
                //Handle success
                adManager.adManagerSettings = AdManagerSettings.Builder() // optional
                    .adPlayerInstance(YOUR_AD_PLAYER_GOES_HERE) // optional
                    .build() // optional
                adManager.setListener(this) // Get notifications from the Ad SDK
                adManager.prepare() // Start buffering the ads in the AdManager
            } else {
                //Handle failure
                Log.e("Error", error.toString())
            }
        }
    }

    override fun onEventErrorReceived(adManager: AdManager, ad: AdData?, error: Error) {
    }

    override fun onEventReceived(adManager: AdManager, event: AdEvent) {
        when(event.type) {
            AdEvent.Type.State.DidFinishLoading ->  {
                adManager.play() //Start playing the next ad in the AdManager
            }
            AdEvent.Type.State.DidFinishPlaying -> {
                // Current ad has finished playing
            }
            AdEvent.Type.State.AllAdsDidFinishPlaying -> {
                // All ads from the AdManager have finished
            }

            else  -> {
                //do nothing
                //If you want to handle other events individually you can use the same syntax as above,
                //just specify the event type
            }
        }
    }
}
```
To actually start the **_AdManager_** rolling the ads you must call the play method on the **_AdManager_** as we did in the example above.

## AdManager operations

Once presented with an AdManager, one could call different actions on the AdManager. Let’s break them down.

## AdManager interface
### prepare

You call this method to begin to cycle through the ads in the AdManager. If you decided to let the SDK handle the
playing of the ads this method ensures that the internal player is starting to buffer enough data so that ad playing
starts smoothly. Upon calling this method the first ad starts loading. The SDK will trigger
**_WillStartLoading_** event informing your app that buffering has begun for the ad. Once buffering is done, **_DidFinishLoading_** event for the first ad will be triggered.


### play

Call **_play_** when you want to play the ads. This should be done after the callback **_DidFinishLoading_** was triggered. The SDK will respond with the callback **_DidStartPlaying_**. If the playing was pause use **_resume_** function instead, to resume playing.


### pause

The **_pause_** method will stop playing the ads in the AdManager. AdManager will trigger a **_DidPausePlaying_** event back to your app for confirmation.


### resume

Call **_resume_** when you want to play the ads after a pause. The SDK will trigger a **_DidResumePlaying_** event back to your app for confirmation.


### skipAd

If you need to skip an ad you can call this method to skip the current ad from the AdManager. Your app will receive
a **_DidSkip_** event for the current ad and if the AdManager has a new ad you will receive
**_WillStartLoading_** for that one. If no ads are available, an **_AllAdsDidFinishPlaying_** will be sent,
signaling that all ads got processed in the AdManager.


### reset

If you decide to skip all ads in the AdManager from the current one you can call this method. For each ad skipped
your app will trigger **_DidSkip_** and a **_AllAdsDidFinishPlaying_** event will be sent at the end.
Looping through the ad again will need a call to **_prepare_** function.</br>

Below is a descriptive graph with all this information:

</br></br>


<img src="img/AdManagerState.png" width="1000" />


# Server-Side Insertion


## Your first stream manager


To get started, you need to create an **_AdswizzAdStreamManager_** object with a URL pointing to the ad server your Integration Manager provided you with.


```kotlin

import com.adswizz.core.streaming.AdswizzAdStreamManager

class YourClass {

    private var streamManager: AdswizzAdStreamManager? = null

    fun createStreamManager() {
        streamManager = AdswizzAdStreamManager(null)
    }
}

```

Once the stream manager is constructed it is recommended to set a listener:

```kotlin
class YourClass {

    private var streamManager: AdswizzAdStreamManager? = null

    private val listener = object : AdStreamManager.Listener {
        override fun willStartPlayingUrl(adStreamManager: AdStreamManager, url: Uri) {
            println("Will start playing url: $url")
        }

        override fun didFinishPlayingUrl(adStreamManager: AdStreamManager, url: Uri) {
            println("Did finish playing url: $url")
        }

        override fun didPausePlayingUrl(adStreamManager: AdStreamManager, url: Uri) {
            println("Did paused playing url: $url")
        }

        override fun didResumePlayingUrl(adStreamManager: AdStreamManager, url: Uri) {
            println("Did resume playing url: $url")
        }

        override fun adBreakStarted(adStreamManager: AdStreamManager, adBaseManager: AdBaseManager) {
            println("Ad break started: adBaseManager $adBaseManager")
        }

        override fun adBreakEnded(adStreamManager: AdStreamManager, adBaseManager: AdBaseManager) {
            println("Ad break ended: adBaseManager $adBaseManager")
        }

        override fun onError(adStreamManager: AdStreamManager, error: Error) {
            println("Error - ${error.message} for adStreamManager: $adStreamManager")
        }

        override fun onMetadataChanged(adStreamManager: AdStreamManager, metadataItem: AdPlayer.MetadataItem) {
            println("Metadata received - adStreamManager: $adStreamManager - metadata count: ${metadataItem.value.count()}")
        }
    }

    fun createStreamManager() {
        streamManager = AdswizzAdStreamManager(null)
        streamManager?.addListener(listener)
    }
}

```

The stream object can play the url using his internal player or using an external player provided by you. Below is a sample on how to set the external player:


```kotlin
    fun createStreamManager() {
        val settings = AdManagerStreamingSettings.Builder().adPlayerInstance(externalPlayer).build()
        streamManager = AdswizzAdStreamManager(settings)
        ...
    }
```

To start playing the stream do the following:

```kotlin
    streamManager?.play(AIS_URL_PROVIDED_BY_INTEGRATION_MANAGER)
```

As a response, **_AdswizzSDK_** will call back `fun willStartPlayingUrl(adStreamManager: AdStreamManager, url: Uri)` with the provided url that has some extra query params added.

To stop the playing of stream call the stop function:

```kotlin
    streamManager?.stop()
```

The SDK will respond with the callback `fun didFinishPlayingUrl(adStreamManager: AdStreamManager, url: Uri)`. The url is the same as for `willStartPlayingUrl`.

The stream can be paused and resumed:

```kotlin
    ...
    streamManager?.pause()
    ...
    streamManager?.resume()
    ...
```

The SDK will respond with the callbacks `fun didPausePlayingUrl(adStreamManager: AdStreamManager, url: Uri)` and `fun didResumePlayingUrl(adStreamManager: AdStreamManager, url: Uri)` respectively. The url is the same as for `willStartPlayingUrl`.


## AdStreamManager Listener interface

The available callbacks that are called by the stream manager are described below:

```kotlin
    interface Listener {
        fun willStartPlayingUrl(adStreamManager: AdStreamManager, url: Uri)
        fun didFinishPlayingUrl(adStreamManager: AdStreamManager, url: Uri)
        fun didPausePlayingUrl(adStreamManager: AdStreamManager, url: Uri)
        fun didResumePlayingUrl(adStreamManager: AdStreamManager, url: Uri)
        fun adBreakStarted(adStreamManager: AdStreamManager, adBaseManager: AdBaseManager)
        fun adBreakEnded(adStreamManager: AdStreamManager, adBaseManager: AdBaseManager)
        fun onMetadataChanged(adStreamManager: AdStreamManager, metadataItem: AdPlayer.MetadataItem)
        fun onError(adStreamManager: AdStreamManager, error: Error)
    }
```

### fun willStartPlayingUrl(adStreamManager: AdStreamManager, url: Uri)
After executing the play function on the stream manager object, the SDK will call this function with the original url decorated with extra parameters.

### fun didFinishPlayingUrl(adStreamManager: AdStreamManager, url: Uri)
After executing the stop function on the stream manager object, the SDK will call this function with the original url decorated with extra parameters. The decorated url will be the same as the one in the `willStartPlayingUrl` callback.

### fun didPausePlayingUrl(adStreamManager: AdStreamManager, url: Uri)
After executing the pause function on the stream manager object, the SDK will call this function with the original url decorated with extra parameters. The decorated url will be the same as the one in the `willStartPlayingUrl` callback.

### fun didResumePlayingUrl(adStreamManager: AdStreamManager, url: Uri)
After executing the resume function on the stream manager object, the SDK will call this function with the original url decorated with extra parameters. The decorated url will be the same as the one in the `willStartPlayingUrl` callback.

### fun adBreakStarted(adStreamManager: AdStreamManager, adBaseManager: AdBaseManager)
When an ad break is detected in the stream, the SDK will execute this callback. It will provide an **_AdBaseManager_** object that can be used for the whole duration of the ad break. You can use it to listen to different Ad related events and also to request a skip of current ad.
When you get this **_AdBaseManager_** object, the player will automatically play the ad, since it is part of the stream. Most of the times there will be only one ad in the **_AdBaseManager_** object but you can expect other ads to be inserted from the SDK if the ad is extended. This can happen, for instance, when the listener interacts with an ad (i.e. ShakeMe) and the action is to play an extension of the ad.

### fun adBreakEnded(adStreamManager: AdStreamManager, adBaseManager: AdBaseManager)
When the ad break from the stream has ended you will get notified to clean up anything related to the provided **_AdBaseManager_**.

### fun onMetadataChanged(adStreamManager: AdStreamManager, metadataItem: AdPlayer.MetadataItem)
Whenever the metadata is changed on the stream played by the SDK, you will be notified with this callback.

### fun onError(adStreamManager: AdStreamManager, error: Error)
When an error occurs during your interaction with the stream manager this callback will be called by the SDK.


# Interactive ads

AdsWizz interactive ads require some permissions on your app.

```kotlin
    <uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.WRITE_CONTACTS" />
    <uses-permission android:name="android.permission.WRITE_CALENDAR" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.VIBRATE" />
```

## Handling interactive ad events

While the **_AdManger.Listener_** provides a list of **_AdEventType_** covering the life cycle of an ad it does not provide information on an interactive ad.
To get more insight on what is happening while an interactive ad is playing you can set the **_InteractivityListener_**.
To set the listener, add the following line of code:
```kotlin
AdswizzSDK.setInteractivityListener(adManager, interactivityListener)
```

A basic implementation of the **_InteractivityListener_** could looks something like this:

```kotlin
class MyInteractivityListener : InteractivityListener {
    override fun onReceiveInteractivityEvent(
        adBaseManager: AdBaseManager,
        adData: AdData,
        event: InteractivityEvent
    ) {
        when(event) {
            InteractivityEvent.AD_WILL_BE_SKIPPED -> {
                // Ad will be skipped as a result of an action during interactive ad
            }
            InteractivityEvent.SKIP_AD ->  {
                // Ad was skipped as a result of an action during interactive ad
            }
            InteractivityEvent.EXTEND_AD -> {
                // Ad was extended with a new media as a result of an action
            }
        }
    }

    /**
     * Here you can provide your own implementation for coupon presenting
     * @return true if you want to use your own implementation
     * @return false otherwise
     */
    override fun shouldOverrideCouponPresenting(
        adBaseManager: AdBaseManager,
        couponUri: Uri
    ): Boolean {
        TODO()
    }
}
```

# Companion Banner

AdswizzSDK lets you configure companion banner(s) if you are provided by the Adswizz PIM with a companion zone id.


## Adding an AdCompanionView

The easiest way to add a companion view is from XML. You could also add the view programmatically.
Once created, the SDK will keep track of all of your companion views and fill them with ad related content as the
ad is playing.
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">
.....
    <com.ads.coresdk.companion.AdCompanionView
            android:id="@+id/companionView1"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent"/>
.......
</androidx.constraintlayout.widget.ConstraintLayout>
```
## Setting up

First when you create an **_AdswizzAdRequest_** you must configure the **_companionZones_** with the value provided by the PIM.

```kotlin
val adRequest = AdswizzAdRequest.Builder() //Build the Ad Request with the needed parameters
                .withServer("SERVER_PROVIDED_BY_PIM")
                .withZoneId("ZONEID_PROVIDED_BY_PIM")
                .withPlayerId("PLAYERID_PROVIDED_BY_PIM")
                .withCompanionZones("COMPANION_PROVIDED_BY_PIM")
                .build()
```

Once the request is made, if there is a companion associated with the ad it will be loaded into your **_AdCompanionView_** object.

## Companion events

You can associate a listener to your AdCompanionView that implements the AdCompanionView.Listener interface.
You will be notified when the ad has loaded the companion view, if the companion view was displayed, or if it ended
the display. If there was an error while loading the companion you will be notified through this listener.
Also, you can decide whether or not to override click through on the companion view.

## Extra exposure time

By default the AdCompanionView will end displaying the content after the ad finishes playing.
If you need to keep the companion on the screen for a longer time(or indefinitely) you can configure it like this.

```kotlin
val adCompanionOptions = AdCompanionOptions()
adCompanionOptions.extraExposureTime = 1.2// these are seconds.
AdswizzSDK.setAdCompanionOptions(adCompanionOptions)
```

# Playing ads using your player

AdswizzSDK gives you the possibility to choose whether to play the ad media with your player or let the SDK handle that for you.
By default, the SDK will play the ad. The AdManager object is player agnostic. This means that as long as you provide an
**_AdManagerSettings_** object with an instance of your player before calling ```adManager.prepare()``` the adManager will use your
player to play the ads. Your player must implement the **_AdPlayer_** interface.

## AdPlayer Interface

```kotlin
interface AdPlayer {

    data class MetadataItem(val key: String, val value: String)

    enum class Status {
        // Player state is unknown. Something has happen.
        UNKNOWN,
        // Player is initialized but not playing and does not have an item to play. This should be the default state.
        INITIALIZED,
        // Player is about to begin buffering
        BUFFERING,
        // Player has finished buffering
        BUFFERING_FINISHED,
        // Player is playing the item.
        PLAYING,
        // Player has been paused playing
        PAUSED,
        // Player has finished the whole item. This would be the last state for an item.
        FINISHED,
        // Player failed to load the item
        FAILED,
    }

    fun load(creativeURL: Uri)

    /**
    * Starts the playback of the ad and gives the onPlay/onResume event to the listener
    */
    fun play()

    /**
    * Pauses the playback of the ad and gives the onPause event to the listener
    */
    fun pause()

    /**
    * Resets the player to the initial state
    */
    fun reset()

    /**
     * Reflects the current playback time in seconds for the content.x
     */
    fun getCurrentTime(): Double

    /**
     * Reflects the current track duration from the player
     */
    fun getDuration(): Double?

    /**
     * Reflects the current status for a player.
     */
    fun status(): Status

    /**
    * Used to add a listener to the player's internal state
    */
    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {

        fun onBuffering()

        fun onBufferingFinished()

        fun onPlay()

        fun onPause()

        fun onResume()

        fun onEnded()

        fun onError(error: String)

        fun onMetadata(metadataList: List<MetadataItem>)
    }

}
```
Keep in mind that you need to call the right events on the listener so that the adManager knows to take the right actions.

# AdswizzSDK general settings

## GDPR consent

AdsWizz services are GDPR compliant. As a result, **_AdswizzSDK_** will decorate urls that connect to Adswizz services accordingly to reflect the desired GDPR user consent. To modify the GDPR consent you need to configure it like this:

```kotlin
    AdswizzSDK.gdprConsent = GDPRConsent.GRANTED
```

If not configured, the default value for GDPR consent in **_AdswizzSDK_** will be  **_NOT_APPLICABLE_**. This means that the user is not a subject of the GDPR. Other possible values are:
  * **_NOT_ASKED_**: the user was not asked yet about GDPR consent
  * **_GRANTED_**: the user has granted access
  * **_DENIED_**: the user has denied access

## CCPA config

With the introduction of CCPA in US, **_AdswizzSDK_** gives you the possibility to forward the user consent accordingly.

```kotlin
    AdswizzSDK.ccpaConfig = CCPAConfig(CCPAConsent.YES, CCPAConsent.YES, CCPAConsent.YES)
```

The first value represents the explicit notice for collecting consent. The next value is the opt-out of the sale of his personal information. The last one is the limitation of scope to the Limited Service Provider Agreement. For all tree, possible values are: **_NOT_APPLICABLE_**, **_YES_**, **_NO_**. The default value for all of them is **_NOT_APPLICABLE_**.

## AFR config

In order to have the companion displayed during 'Server-Side Insertion' an AFR Config object should be provided to the SDK. With the information obtained from AdsWizz this object can be configured like in the code snippet below:

```kotlin
    AdswizzSDK.afrConfig = AfrConfig(
            AdswizzAdRequest.HttpProtocol.HTTP,
            "demo.deliveryengine.adswizz.com/",
            "www/delivery/afr.php"
        )
```

## Integrator Context

In order to correctly do macro expansion the integrator should implement the `IntegratorContext` interface and provide it to the SDK.

```kotlin
    AdswizzSDK.integratorContext = IntegratorContextImplementation()
```

The interface definition is provided below:

```kotlin
/**
 * Interface to be implemented by the integrator to provide host app information to the SDK
 */
interface IntegratorContext {

    // the player used to play the content. It may be the same as the ad player or a different one. It may not exist for server-side insertion
    var contentPlayer: AdPlayer?

    // Indicates whether the SDK’s intended use case was video, audio, or hybrid
    var adType: Ad.AdType
}
```

## Extra exposure time for an AdCompanionView

By default, the **_AdCompanionView_** will end displaying the content after the ad finishes playing. If you need to keep the companion on the screen for a longer period of time (or indefinitely), you can configure it like this.

```kotlin
    val adCompanionOptions = AdCompanionOptions(exposureTime)
    AdswizzSDK.setAdCompanionOptions(adCompanionOptions)
```

# (Optional) Prepare your application for advanced targetability capabilities

## Privacy implications

Please be advised that if you choose to enable the Raw Data Signal Collection, you will have to inform your customers that your application handles and processes personal anonymized data. <br>
**We highly encourage you to loop your product and legal teams in the process of defining the best way to reflect this in your application’s Privacy Policy.**

---------------------------------------------------------------------------------------------------------------------------------------------------
| Data collected for <br>**_Device Targeting_** | Data collected for <br>**_Contextual Targeting_** | Data collected for <br>**_User Targeting_**|
|:-----------------|:---------------------|:--------------|
| * device name and locale <br>* screen brightness level<br>* audio volume level<br>* battery level, status and state (charging or not) <br>* bundle id, version name and version code<br>* storage information (available and total capacities) <br>* OS name and version | * bluetooth name, status (on, off, connected etc.) and devices (currently connected, paired and history). For bluetooth devices we collect: name, address, profile and bluetooth class<br>* WiFi Status (true/false), state and WiFi network name / SSID<br>* network carrier name and country<br>* accelerometer, GPS and gyroscope data<br>* headphone jack status (plugged/unplugged)<br>* time zone information (in GMT format)<br>* daylight saving time status (true/false)<br>* uiMode (normal, desk, car, watch, tv etc.)<br>* microphone permission status<br>* name and type of the active audio device and available devices<br>* app permissions<br>* sensors information: type, name, vendor, version, power used by sensor, resolution, minimum delay allowed between two events, maximum rage of the sensor, the maximum number of events that could be batched, the number of events reserved in the batch mode FIFO, maximum delay and the reporting mode<br>* installed app names | * idfa (identifierForAdvertising) status (enabled/disabled) and ID (if enabled) |


# Sample projects

The best way to see the AdswizzSDK in action is by studying the example projects included in the `/samples` folder. Download the `/samples` folder using the below `git` command:
```properties
$ git clone https://github.com/adswizz/ad-sdk-android.git
Username: your_username
Password: Personal_Access_Token_provided
```

## BasicSample

This sample demonstrates a basic client-side insertion scenario by showing how to create and customize an _**AdswizzAdRequest**_. It shows how to use it to create an _**AdRequestConnection**_ and finally request ads. Next, it demonstrates basic usage of an instance of _**AdManager**_ once it is obtained from the SDK.

## StreamingSample

This sample demonstrates a basic server-side insertion scenario by showing how to create and customize an _**AdswizzAdStreamManager**_.
