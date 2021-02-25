package com.revature.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtil
{
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory()
    {
        try
        {
//            try
//            {
//                if (sessionFactory == null) {
//                    StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//                    Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
//                    sessionFactory = metaData.getSessionFactoryBuilder().build();
//                }
//                // Create the SessionFactory from hibernate.cfg.xml
//                return sessionFactory;
//            }
            // Create the SessionFactory from hibernate.cfg.xml
            try {
                //if on the web
                return new Configuration().configure(new File("webapps/test/WEB-INF/classes/hibernate.cfg.xml")).buildSessionFactory();
            } catch (Exception e) {
                return new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml")).buildSessionFactory();
            }

        }

        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
