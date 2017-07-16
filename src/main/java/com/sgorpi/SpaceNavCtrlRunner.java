package com.sgorpi;

import java.lang.Thread;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.UserControlBank;

import net.sf.spacenav.*;




public class SpaceNavCtrlRunner implements Runnable
{
	public enum Mode { ABSOLUTE, RELATIVE };
	
	private SpaceNav sn;
	private Mode ctrlMode;
	
	private Transport transport;
	private Application app;
	
	private UserControlBank userControls;
	
	private final String[] userControlNames = {
		"X", "Y", "Z",
		"Rx", "Ry", "Rz"
	};
	private int[] knobLast3DValue = {
		0, 0, 0, 
		0, 0, 0
	};
	private double[] userControlLast = {
		0.0, 0.0, 0.0,
		0.0, 0.0, 0.0
	};
	
	static final int AXIS_THRESHOLD = 20;
	
	static final double[] AXIS_SCALE = {
		512.0, 512.0, 512.0, 
		512.0, 512.0, 512.0
	};
	static final double[] AXIS_OFFSET = {
		256.0, 256.0, 256.0,
		256.0, 256.0, 256.0
	};
	

	protected SpaceNavCtrlRunner(ControllerHost host)
	{
		sn = new SpaceNav();
		sn.setSensitivity(0.5);
		
		ctrlMode = Mode.RELATIVE;
		
		transport = host.createTransport();
		app = host.createApplication();
		
		userControls = host.createUserControls(6); // 6 axis
		for (int i = 0; i < 6; i++)
			userControls.getControl(i).setLabel(userControlNames[i]);

		//transport.getPosition().markInterested();
	}
	
	
	public void setControl(int ctrlNum, int value) {
		if (value < -AXIS_THRESHOLD) {
			value = value + AXIS_THRESHOLD;
		} else if (value > AXIS_THRESHOLD) {
			value = value - AXIS_THRESHOLD;
		} else {
			value = 0;
		}
		
		if (knobLast3DValue[ctrlNum] != value) { // don't repeatedly set the value, to not grab the mapping
			double vAbs = (AXIS_OFFSET[ctrlNum] - AXIS_THRESHOLD + value) / (AXIS_SCALE[ctrlNum] - 2*AXIS_THRESHOLD);
			double vRel = value / (2*AXIS_SCALE[ctrlNum]);
			
			if (ctrlMode == Mode.ABSOLUTE) {
				userControls.getControl(ctrlNum).set( vAbs );
			} else {
				userControls.getControl(ctrlNum).inc( vRel );
			}
			
			knobLast3DValue[ctrlNum] = value;
		}
	}
	
	
	private void handleEvent(SpaceNavEvent e) {
		if(e instanceof SpaceNavMotionEvent) {
			SpaceNavMotionEvent m = (SpaceNavMotionEvent) e;
		
			//double trPos = transport.getPosition().get();
			//double d = (m.getRY() - THRESHOLD)/SCALE_RY;
			//d = Math.round(d) / 4.0; // 4 counts in a beat
			//transport.setPosition(trPos - d);
			
			setControl(0, m.getX());
			setControl(1, m.getY());
			setControl(2, m.getZ());
			setControl(3, m.getRX());
			setControl(4, -m.getRY());
			setControl(5, m.getRZ());
		}
		else if(e instanceof SpaceNavButtonEvent) {
			SpaceNavButtonEvent b = (SpaceNavButtonEvent) e;

			if (b.isPressed())
				switch(b.getButton()) {
					case 0: // = top row 1
						app.setPanelLayout(Application.PANEL_LAYOUT_ARRANGE);
						break;
					case 1: // = top row 2
						app.setPanelLayout(Application.PANEL_LAYOUT_EDIT);
						break;
					case 2: // = top row 3
						app.setPanelLayout(Application.PANEL_LAYOUT_MIX);
						break;
					case 3: // = top row 4
						break;
					case 4: // = top row 5
						ctrlMode = Mode.ABSOLUTE;
						System.out.println("SpaceNavCtrl - absolute");
						break;
					case 5: // = top row 6
						ctrlMode = Mode.RELATIVE;
						System.out.println("SpaceNavCtrl - relative");
						break;
					case 6: // = T
						break;
					case 7: // = L
						transport.rewind();
						break;
					case 8: // = R
						transport.fastForward();
						break;
					case 9: // = F
					case 10: // = ESC
					case 11: // = ALT
					case 12: // = SHIFT
					case 13: // = CTRL
						break;
					case 14: // = Fit
						app.zoomToFit();
						break;
					case 15: // = Panel
						break;
					case 16: // = Vol+
						app.zoomIn();
						break;
					case 17: // = Vol-
						app.zoomOut();
						break;
					case 18: // = Dom
						break;
					case 19: // = 3D
						transport.togglePlay();
						break;
					case 20: // = Config
					default:
						System.out.printf("Button event: %d %s\n",
							b.getButton(), b.isPressed() ? "pressed" : "released" );
						break;
				}
		}
		else {
			System.out.println("Unknown SpaceNav event!");
		}	
	}
	
	
	public void run() {
		SpaceNavEvent e;
		
		while (!Thread.currentThread().isInterrupted()) {
			if ((e = sn.pollForEvent()) != null) {
				handleEvent(e);
			} else {
				try {
					Thread.sleep(1000/60); // 1 second / 60 Hz
				} catch (InterruptedException except) {
					// good practice
					Thread.currentThread().interrupt();
				}
			}
		}
	    sn.closeDevice();
	}
}

