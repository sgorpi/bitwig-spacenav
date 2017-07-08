# Bitwig Spacenav controller extension

This repository holds a Bitwig extension for 3Dconnexion devices (space navigator", "space pilot", "space traveller", etc), using the free Spacenav device driver from http://spacenav.sourceforge.net/

Installation (the spacenav library must already be installed):
```
### first the build:
mvn install
### then copy the files to the correct location
mkdir -p "$HOME/Bitwig Studio/Extensions/SpaceNav"
cp target/SpaceNavCtrl.bwextension "$HOME/Bitwig Studio/Extensions/SpaceNav"
sudo cp target/classes/libspnav_jni.so /usr/lib/
```

Note, the native part of the spacenav java library should be installed in a path that the Bitwig Java runtime searches. This seems to be in, among others, `/usr/lib/`



