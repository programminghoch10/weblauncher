# weblauncher
Weblauncher is a startscreen launcher which only has the purpose to display a website, made for kiosk purposes.

__This version is made only for rooted devices!__

[no root version](https://github.com/programminghoch10/weblauncher/tree/norootversion)



## Features:
- 2 webpages switchable using volume keys
- reload webpage using volume keys
- always uses current webpage due to disabled cache
- auto enables wifi if disabled
- full screen
- acts as app launcher to be the only launchable application (kiosk mode)
- pulls 2 website urls from build.prop (rooted device needed/subject to change)
  - build.prop properties JJ.Page1 and JJ.Page2
  - if those arent defined, google.de and example.com are used

[Download APK](https://github.com/programminghoch10/weblauncher/raw/master/app/release/app-release.apk)

## Install
1. Install the app by downloading on target device or by pushing via adb
1. set the build.prop parameters "JJ.Page1" and "JJ.Page2" to the wished 2 websites you want to be used (and reboot)
1. test the app by launching the weblauncher through your launcher
1. if everything works fine set weblauncher as default launcher
1. for complete kiosk mode disable apps like system settings, camera and other apps which may be launched using lockscreen, keys or the notification bar
1. _You're done!_
