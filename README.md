# Bitwig Spacenav controller extension

This repository holds a Bitwig extension for 3Dconnexion devices (space navigator", "space pilot", "space traveller", etc), using the free Spacenav device driver from http://spacenav.sourceforge.net/

# Installation
The spacenav library and daemon must already be installed. The java library is included here for convenience.

First, build the extension and native library:
```
mvn install
```

Then, copy the files to the correct locations.
```
mkdir -p "$HOME/Bitwig Studio/Extensions/SpaceNav"
cp target/SpaceNavCtrl.bwextension "$HOME/Bitwig Studio/Extensions/SpaceNav"
sudo cp target/classes/libspnav_jni.so /usr/lib/
```

Note, the native part of the spacenav java library should be installed in a path that the Bitwig Java runtime searches. This seems to be in, among others, `/usr/lib/`



