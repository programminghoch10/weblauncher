# weblauncher
Weblauncher is a startscreen launcher which only displays a website, made for kiosk purposes.

___Please read the install procedure for proper install and use!___
The download link is at the end of it.

__This version is made only for rooted devices!__

soon there will be a proper [no root version](https://github.com/programminghoch10/weblauncher/tree/norootversion)


## Features:
- 2 webpages switchable using volume keys
- reload webpage using volume keys
- always uses current webpage due to disabled cache
- auto enables wifi if disabled
- full screen
- acts as app launcher to be the only usable application (kiosk mode)
- pulls 2 website urls from build.prop (rooted device needed/subject to change)
  - build.prop properties JJ.Page1 and JJ.Page2
  - if those aren't defined, a blank page is displayed (about:blank)
  
## Install
1. Install the app by downloading on target device or by pushing via adb
1. set the build.prop parameters "com.JJ.weblauncher.page1.url" and "com.JJ.weblauncher.page2.url" to the wished 2 websites you want to be used (and reboot)
1. test the app by launching the weblauncher through your launcher
1. if everything works fine set weblauncher as default launcher
1. for complete kiosk mode disable apps like system settings, camera and other apps which may be launched using lockscreen, keys or the notification bar using adb and the pm disable command
1. _You're done!_

[Download APK](https://github.com/programminghoch10/weblauncher/raw/rootedversion/app/release/app-release.apk)

## How to kiosk

Here will be a section explaining how to properly use kiosk with this app soon.