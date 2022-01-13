# SpringIntegrationTest

I'm trying to initialize an OSGi bundle jar from spring service. OSGi bundle register a service called Activator, once OSGi bundle initialized and Activator created, trying to access the Activator from Spring.

Note : OSGi bundle is another module, it is added part of this project for testing purpose. Spring App only need a OSGi bundle as a jar.

How to run : curl http://localhost:8080/osgi

Issues facing : Spring App while trying to access Activator, it is throwing ClassCastException
