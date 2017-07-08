package com.sgorpi;

import java.lang.Thread;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.api.Application;

import net.sf.spacenav.*;


public class SpaceNavCtrlThread extends Thread
{
	private SpaceNav sn;
	private Transport transport;
	private Application app;
	
	static final int THRESHOLD = 20;
	static final double SCALE_RY = 50.0;
	
	private volatile boolean running;
	
	protected SpaceNavCtrlThread(ControllerHost host)
	{
		sn = new SpaceNav();
		sn.setSensitivity(0.5);
		
		transport = host.createTransport();
		app = host.createApplication();

		transport.getPosition().markInterested();
	}
	
	public void terminate() { running = false; }

	public void run() {
		SpaceNavEvent e;
		running = true;
		
		while (running && ((e = sn.waitForEvent()) != null)) {
			if(e instanceof SpaceNavMotionEvent) {
				SpaceNavMotionEvent m = (SpaceNavMotionEvent) e;
			
				double trPos = transport.getPosition().get();
			
				if (m.getX() > THRESHOLD) {
					app.zoomIn();
				} else if (m.getX() < -THRESHOLD) {
					app.zoomOut();
				}
			
				if (m.getRY() > THRESHOLD) {
					double d = (m.getRY() - THRESHOLD)/SCALE_RY;
					d = Math.round(d) / 4.0; // 4 counts in a beat
					transport.setPosition(trPos - d);

				} else if (m.getRY() < -THRESHOLD) {
					double d = (m.getRY() + THRESHOLD)/SCALE_RY;
					d = Math.round(d) / 4.0;
					transport.setPosition(trPos - d);
				} else {
					System.out.printf("Motion event: tx:%4d ty:%4d tz:%4d rx:%4d ry:%4d rz:%4d\n",
						m.getX(), m.getY(), m.getZ(),
						m.getRX(), m.getRY(), m.getRZ()
					);
				}
			}
			else if(e instanceof SpaceNavButtonEvent) {
				SpaceNavButtonEvent b = (SpaceNavButtonEvent) e;

				System.out.printf("Button event: %d %s\n",
					b.getButton(), b.isPressed() ? "pressed" : "released" );

//				0 - 5 = top nr'ed row
//				6 - T 
//				7 - L
//				8 - R
//				9 - F
//				10 -esc
//				11 -alt 
//				12 -shift
//				13 -ctrl
//				14 -Fit
//				15 -panel
//				16 -vol+
//				17 -vol-
//				18 -Dom
//				19 -3D
//				20 -config
				if (b.isPressed())
					switch(b.getButton()) {
						case 0:
							app.setPanelLayout(Application.PANEL_LAYOUT_ARRANGE);
							break;
						case 1:
							app.setPanelLayout(Application.PANEL_LAYOUT_EDIT);
							break;
						case 2:
							app.setPanelLayout(Application.PANEL_LAYOUT_MIX);
							break;
						case 14:
							app.zoomToFit();
							break;
						case 19: // -3D
							transport.togglePlay();
							break;
						default:
							break;
					}
			}
			else {
				System.out.println("Unknown event!");
			}
		}

	    sn.closeDevice();
	}
}
