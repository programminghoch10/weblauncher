# weblauncher
This is a launcher which goes into fullscreen to show a website, used for kiosk applications

This launcher is made to only show websites in kiosk mode.

Features:
- 2 webpages switchable using volume keys
- reload webpage using volume keys
- always uses current webpage due to disabled cache
- auto enables wifi if disabled
- full screen
- acts as app launcher to be the only launchable application (kiosk mode)
- pulls 2 website urls from build.prop (rooted device needed/subject to change)
  - build.prop properties JJ.Page1 and JJ.Page2
  - if those arent defined, google.de and example.com are used

[Test APK](https://github.com/programminghoch10/weblauncher/raw/master/app/release/app-release.apk)
