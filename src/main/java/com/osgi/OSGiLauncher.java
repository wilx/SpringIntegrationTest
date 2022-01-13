package com.osgi;

import org.apache.felix.framework.FrameworkFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.springframework.stereotype.Component;
import com.myInterface.BundleInterface;
import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.ServiceLoader;

@Component
public class OSGiLauncher {
    BundleContext bc;
    public void init() {


        try {
            ServiceLoader loader = ServiceLoader.load(FrameworkFactory.class);
            FrameworkFactory factory = getFrameworkFactory();
            // Create a new instance of the framework
            Framework framework = factory.newFramework(null);
            // Start the framework
            framework.init();
            framework.start();
            bc = framework.getBundleContext();

            bc.addBundleListener((event) -> {
                System.out.println("Bundle Changed Event:"+event.getType()+":Name:"+event.getBundle().getSymbolicName());
                int type = event.getType();
                if(type==ServiceEvent.REGISTERED || type==ServiceEvent.MODIFIED) {
                    if(null!=event.getBundle().getBundleContext()) {
                        ServiceReference ref = event.getBundle().getBundleContext().getServiceReference(BundleInterface.class.getName());
                        if(null!=ref) {
                            BundleInterface service = (BundleInterface) event.getBundle().getBundleContext().getService(ref);
                            service.execute("Success");
                            System.out.println("Using child BundleContext");
                        }
                    }
                    ServiceReference ref=bc.getServiceReference(BundleInterface.class.getName());
                    if(null!=ref) {
                        BundleInterface service = (BundleInterface) bc.getService(ref);
                        service.execute("Success");
                        System.out.println("Using Parent BundleContext");
                    }
                }

            });
            System.out.println("BundleID = " + framework.getBundleId());
            System.out.println("State = " + getState(framework.getState()));
            System.out.println("Name = " + bc.getBundle().getSymbolicName());
            URL url = this.getClass().getClassLoader().getResource("OsgiBundleTest-0.0.1-SNAPSHOT.jar");
            String file = "file:"+url.getPath();
            //For testing moved jar to resource and loading it
            System.out.println("File Path"+file);
            Bundle childBundle=bc.installBundle(file);
            childBundle.start();
            System.out.println("ChildBundleID = " + childBundle.getBundleId());
            System.out.println("ChildState = " + getState(childBundle.getState()));
            System.out.println("ChildName = " + childBundle.getSymbolicName());
//            Collection<ServiceReference<BundleInterface>> references = bc
//                    .getServiceReferences(BundleInterface.class, null);
//
//            for (ServiceReference<BundleInterface> reference : references) {
//            	BundleInterface bi = bc.getService(reference);
//            	bi.execute("Success");
//            	System.out.println("Using Parent BundleContext");
//            }
//			try {
//				ServiceReference<?>  ref = childBundle.getBundleContext().getServiceReference(BundleInterface.class.getName());
//				if (null != ref) {
//					BundleInterface service = (BundleInterface) childBundle.getBundleContext().getService(ref);
//					service.execute("Success");
//	            	System.out.println("Using child BundleContext");
//				}
//			} catch (Exception e) {
//				System.out.println("Exception:" + e.getMessage());
//			}

            for (Bundle b : bc.getBundles()){
                System.out.println(b.getSymbolicName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private  FrameworkFactory getFrameworkFactory() throws Exception
    {
        java.net.URL url = OSGiLauncher.class.getClassLoader().getResource(
                "META-INF/services/org.osgi.framework.launch.FrameworkFactory");
        if (url != null)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            try
            {
                for (String s = br.readLine(); s != null; s = br.readLine())
                {
                    s = s.trim();
                    // Try to load first non-empty, non-commented line.
                    if ((s.length() > 0) && (s.charAt(0) != '#'))
                    {
                        return (FrameworkFactory) Class.forName(s).newInstance();
                    }
                }
            }
            finally
            {
                if (br != null) br.close();
            }
        }

        throw new Exception("Could not find framework factory.");
    }

    private static String getState(int state) {
        switch (state) {
            case Bundle.UNINSTALLED:
                return "UNINSTALLED";
            case Bundle.INSTALLED:
                return "INSTALLED";
            case Bundle.RESOLVED:
                return "RESOLVED";
            case Bundle.STARTING:
                return "STARTING";
            case Bundle.STOPPING:
                return "STOPPING";
            case Bundle.ACTIVE:
                return "ACTIVE";
            default:
                throw new IllegalStateException("Unknown state");
        }
    }
}

