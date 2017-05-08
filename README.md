# README

## Android Studio version
2.3.1

## Minimum Android SDK version
22

# Smart Community
This is an application for the Miami-Dade area to be able to report, view, and vote on community issues within their county. It consists of an Android mobile app and a backend created with Ruby on Rails that includes an API and a MySQL database. This readme document details the android application part of the Smart Community application.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites
#### What you need, and how to install it.

* Android Studio IDE:
	
	[Click here to install Android Studio](https://developer.android.com/studio/install.html)

* Android device or Android Virtual Device for viewing
  * Connect Android device via usb to computer where app is downloaded
  * Android Virtual Device included in Android Studio. If no previously installed Virtual Device:
    * Tools > Android > AVD Manager
    * Create Virtual Device
    * Select Nexus 6P (or other desired model) and click next
    * Select a system image with API level higher than minimum required version and click next
    * Name the Virtual Device and click finish

### Installing
#### Installing app for Android Studio and emulator
* Clone app from SmartCommunityMiami’s GitHub in the desired directory via the command line:

`git clone https://github.com/SmartCommunityMiami/smartcommunity_app.git`

* Open the project on Android Studio
  * File > Open > Select project directory name
  
* Insert AWS Acces Key and Secret Key to be able to post and get photos
  * Select `ReportFragment.java`
  * Search for `ACCESS_KEY` and insert access key
  * Search for `SECRET_KEY` and insert secret key
  * Save file
  
* Run the app
  * Run > Run 'app'
  
* Connect your Android Phone via USB if you wish to run and install the application on your device

* Android Studio will provide a dialogue to select a device or virtual device to run the app on

* On the device, real or virtual, navigate to:
  * Settings > Apps > SmartCommunity Mobile
  * Enable camera, storage and location 

* App should run successfully with these permissions enabled
  * Last version of app that is run on device will be stored there for future use
  
### Installing app for Android Mobile Device
* Download APK from Smart Community's Github onto mobile device

## Versioning
* Version 1.0

## Authors
* Conor Murray
* Blake Maune
* Rob Highbloom

## Contributors
* Amanda Hillegass
* Tami Lake
* Toni Boltz
* Stephen Psaradellis
* Sean Loftus
* Harper Chalat	

## License 
* This project is licensed under the Apache License 2.0 – See [LICENSE.txt](https://github.com/SmartCommunityMiami/smartcommunity_app/blob/master/LICENSE.txt) for more info.
