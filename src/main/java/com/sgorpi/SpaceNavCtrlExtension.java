package com.sgorpi;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.ControllerExtension;

import net.sf.spacenav.*;


import java.util.Map;


public class SpaceNavCtrlExtension extends ControllerExtension
{
	SpaceNavCtrlThread spaceNavThread;

	protected SpaceNavCtrlExtension(final SpaceNavCtrlExtensionDefinition definition, final ControllerHost host)
	{
		super(definition, host);
	}
	
	@Override
	public void init()
	{
		final ControllerHost host = getHost();

		try {
			spaceNavThread = new SpaceNavCtrlThread(host);
			spaceNavThread.start();
		} catch(Exception any) {
		 	String javaLibPath = System.getProperty("java.library.path");
			System.out.println("Put libspnav_jni.so in one of the following paths:");
	        System.out.println(javaLibPath);
		}
	}

	@Override
	public void exit()
	{
		spaceNavThread.terminate();
		try {
			spaceNavThread.join();
		} catch(InterruptedException intrEx) {
		}
	}

	@Override
	public void flush()
	{
		// TODO Send any updates you need here.
	}


}
