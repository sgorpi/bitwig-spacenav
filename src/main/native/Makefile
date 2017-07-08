JNI_NAME = SpaceNav
JAVA_HOME = /usr/lib/jvm/default-java

SPNAV_CFLAGS = -I../libspnav/
SPNAV_LDFLAGS = -lspnav -lX11


PACKAGE		:= net.sf.spacenav
JNI_DIR         := $(subst .,/,$(PACKAGE))

JNI_HEADER	:= $(subst .,_,$(PACKAGE))_$(JNI_NAME).h
NATIVE_LIB	:= libspnav_jni.so

all:	testjava $(NATIVE_LIB)
testjava:
	@if [ ! -e $(JAVA_HOME) ]; then \
		echo "Please set JAVA_HOME to point to a SDK!"; \
		exit 1; \
	fi


clean:
	rm -f $(JNI_HEADER) \
	      $(NATIVE_LIB)

$(JNI_HEADER): ../../../target/classes/$(JNI_DIR)/$(JNI_NAME).class
	$(JAVA_HOME)/bin/javah -classpath ../../../target/classes/ -jni $(PACKAGE).$(JNI_NAME)

$(NATIVE_LIB):	$(JNI_HEADER) $(JNI_NAME).c
	gcc -I$(JAVA_HOME)/include \
	    -I$(JAVA_HOME)/include/linux \
	    $(SPNAV_CFLAGS) \
	    -shared \
	    -fPIC \
	    -o ../../../target/classes/$@ $(JNI_NAME).c \
	    $(SPNAV_LDFLAGS)


.PHONY:	clean testjava documentation
