package com.sgorpi;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.ControllerExtension;

import net.sf.spacenav.*;

import java.util.Map;


public class SpaceNavCtrlExtension extends ControllerExtension
{
	SpaceNavCtrlRunner spaceNavCtrlRunner;
	Thread spaceNavCtrlThread;


	protected SpaceNavCtrlExtension(final SpaceNavCtrlExtensionDefinition definition, final ControllerHost host)
	{
		super(definition, host);
	}
	

	@Override
	public void init()
	{
		final ControllerHost host = getHost();

		try {
			spaceNavCtrlRunner = new SpaceNavCtrlRunner(host);
			spaceNavCtrlThread = new Thread(spaceNavCtrlRunner);
			spaceNavCtrlThread.start();
			
		} catch(Exception any) {
		 	String javaLibPath = System.getProperty("java.library.path");
			System.out.println("Put libspnav_jni.so in one of the following paths:");
	        System.out.println(javaLibPath);
		}
	}


	@Override
	public void exit()
	{
		spaceNavCtrlThread.interrupt();
		try {
			spaceNavCtrlThread.join();
		} catch(InterruptedException intrEx) {
			System.out.println("SpaceNavCtrl couldn't join");
		}
	}


	@Override
	public void flush()
	{
		// TODO Send any updates you need here.
	}


}
