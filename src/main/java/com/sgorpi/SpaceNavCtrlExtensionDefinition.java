package com.sgorpi;
import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class SpaceNavCtrlExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("3fdced93-69f6-4284-b915-8a2362b04c30");
   
   public SpaceNavCtrlExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "SpaceNavCtrl";
   }
   
   @Override
   public String getAuthor()
   {
      return "Sgorpi";
   }

   @Override
   public String getVersion()
   {
      return "0.1";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor()
   {
      return "Sgorpi";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "SpaceNavCtrl";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 3;
   }

   @Override
   public int getNumMidiInPorts()
   {
      return 0;
   }

   @Override
   public int getNumMidiOutPorts()
   {
      return 0;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
   }

   @Override
   public SpaceNavCtrlExtension createInstance(final ControllerHost host)
   {
      return new SpaceNavCtrlExtension(this, host);
   }
}
