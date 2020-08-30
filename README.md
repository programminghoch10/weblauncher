# weblauncher
Weblauncher is a startscreen launcher which only displays a website, made for kiosk purposes.

___Please read the install procedure for proper install and use!___
The download link is at the end of it.

__This version is made only for rooted devices!__

~~soon there will be a proper [no root version](https://github.com/programminghoch10/weblauncher/tree/norootversion)~~
Soon it will be usable on unrooted devices too.


## Features:
- 2 webpages switchable using volume keys
- reload webpage using volume keys
- always uses current webpage due to disabled cache
- auto enables wifi if disabled
- full screen
- acts as app launcher to be the only usable application (kiosk mode)
- pulls website settings from build.prop (rooted device needed/subject to change)
  
## Install

1. Install the app by downloading on target device or by pushing via adb
1. set the [build.prop](https://www.droidviews.com/edit-build-prop-file-on-android/) parameters "com.JJ.weblauncher.page1.url" and "com.JJ.weblauncher.page2.url" to the wished 2 websites you want to be used (and reboot)
1. test the app by launching the weblauncher through your launcher
1. if everything works fine set weblauncher as default launcher
1. for complete kiosk mode disable apps like system settings, camera and other apps which may be launched using lockscreen, keys or the notification bar using adb and the pm disable command
1. _You're done!_

[Download APK](https://github.com/programminghoch10/weblauncher/raw/rootedversion/app/release/app-release.apk)

## build.prop parameters

Website configuration parameters within the build.prop need to match this scheme: 

`com.JJ.weblauncher.pageX.parameter`

`pageX` can either be `page1` to modify the first website or `page2` to modify the second website.

`parameter` can be one of the following: 

Parameter | Type | Description | Default Value
--------- | ---- | ----------- | -------------
enabled | Boolean| Wether this page is enabled | true
url | String | The URL of the Website shown, remember the leading `http://` | about:blank
interact | Boolean| Wether this page is interactable | true
unload | Boolean| Wether the Website should be unloaded, when the other page is shown (it will reload to URL when its visible again) | false
js | Boolean| Wether to enable JavaScript on the Website | true
cache | Boolean| Wether to enable Caching for the Website (Recommended: false) | false
haptic | Boolean| Wether Haptic Feedback is enabled on the Website | false
back | Boolean| Wether the back button can be used on the Website | false
useragent | String | The useragent string weblauncher should report to the website | (device default)


Other parameters: 

Parameter | Type | Description | Default Value
--------- | ---- | ----------- | -------------
com.JJ.weblauncher.WiFi.keepon      | Boolean | Wether app will reactivate WiFi, when disabled by user (_only enabled, when weblauncher is set as android home launcher_) | false
com.JJ.weblauncher.startpage        | Integer | Which page to display when starting: 0 = no page, 1 = Page1, 2 = Page2 | 0
com.JJ.weblauncher.display.keepon   | Boolean | Wether to keep screen on | false

## Problems

I always try to keep everything working flawlessly.
But sometimes it's just not in my hands.

* Android 5 might have issues regarding JS processing

If you find other issues (_not listed here_) I'll be happy to take a look at them.
Please report them in this repo's issues.

## How to properly make a kiosk

Here will be a section explaining how to properly use kiosk with this app soon.