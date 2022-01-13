package com.bundle;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.myInterface.BundleInterface;

public class Activator implements BundleActivator, BundleInterface {
    private BundleContext context;
    private ServiceReference<BundleInterface> reference;
    private ServiceRegistration<BundleInterface> registration;
    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Activator start...");
        this.context = context;
        registration = context.registerService(BundleInterface.class, new Activator(), null);
        reference = registration.getReference();

    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Activator stop...");

    }

    @Override
    public void execute(String name) {
        System.out.println("Execute:"+name);

    }

}
