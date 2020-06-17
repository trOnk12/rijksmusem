# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
## [7.1.0] - 2020-05-08

### Added
- SDK support for **WearOS** capable of voice and shake detection
- Support for **InApp Notification** detector
- Support for **Voice** Detector
- Support for **multiple zone request** of ads from AdsWizz Ad Server.
- Support for multiple VAST Macros: TIMESTAMP, CACHEBUSTING, CONTENTPLAYHEAD, MEDIAPLAYHEAD, BREAKPOSITION, BLOCKEDADCATEGORIES,
 ADCOUNT, TRANSACTIONID, ADTYPE, IFA, IFATYPE, CLIENTUA, DEVICEUA, SERVERSIDE, APPBUNDLE, VASTVERSIONS, PLAYERCAPABILITIES,
 PLAYERSTATE, ADPLAYHEAD, ASSETURI, ADSERVINGID, ERRORCODE, LIMITADTRACKING
- Support for various VAST Events: skip, error, loaded, notUsed
- Support to list all VAST companions in AdData interface
- Improved integration documentation
- Added changelog
- Added **AdswizzAdZone** class
- Added **PlayerState** class
- Added **PlayerCapabilities** class
- Added **volume**, **name**, **playerState** and **playerCapabilities** to AdPlayer interface
- Added two new statuses for AdPlayer.Status: **LOADING**, **LOADING_FINISHED**
- ```withZones(zones: Set<AdswizzAdZone>)``` to *AdswizzAdRequest.Builder*
- Added new interface **ModuleLifecycle**
- Added new interface **ModuleLifecycleConfig**

### Removed
- ```withZoneId(zoneId: String)``` from *AdswizzAdRequest.Builder*
- removed ```withDuration(duration: Double?)``` from *AdswizzAdRequest.Builder*

## [7.0.0] - 2020-02-27
### Added
- Brand new AddswizzSDK written in **Kotlin**
- Support for **Java**
- Support for **GDPR** and **CCPA** consent
### Changed
- New implementation with breaking changes. Requires integration from scratch of the new **SDK**.