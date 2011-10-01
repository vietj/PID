import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.net.URL;
import java.util.Random;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class Main
{

   public static void main(String[] args) throws Exception
   {
      // Set a marker marker to recognize our selves through when the agent is executed
      String marker = "" + new Random().nextLong();
      System.setProperty("MARKER", marker);

      // Get our jar
      URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
      String location = new java.io.File(url.toURI()).getAbsolutePath();

      //
      for (VirtualMachineDescriptor desc : VirtualMachine.list())
      {
         System.out.println("About to attach to " + desc.displayName() + " " + desc.id());
         try
         {
            VirtualMachine vm = VirtualMachine.attach(desc);
            vm.loadAgent(location, marker);
            if ("TRUE".equals(System.getProperty("FOUND")))
            {
               System.out.println("My PID is " + desc.id());
               break;
            }
         }
         catch (Exception e)
         {
            System.out.println("Could not attach to " + desc.displayName());
         }
      }
   }

   public static void agentmain(String agentArgs, java.lang.instrument.Instrumentation inst) throws Exception
   {
      String marker = System.getProperty("MARKER");
      if (marker.equals(agentArgs))
      {
         System.setProperty("FOUND", "TRUE");
      }
   }
}
