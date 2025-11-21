package org.ismail.Tricol;

public class TricolApplication {
    // Application class kept for compatibility. With a WAR + web.xml, the servlet container
    // will start Spring MVC using /WEB-INF/applicationContext.xml. No Spring Boot here.

    public static void main(String[] args) {
        System.out.println("TricolApplication main invoked. This project is packaged as a WAR and is intended to run in a servlet container (use Jetty/Tomcat or deploy the WAR).");
    }
}
